package apprentissage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.ejml.simple.SimpleMatrix;

import extraction.Feature;
import misc.Const;
import misc.PDF;
import misc.ShutdownThreads;

import com.sun.glass.ui.Size;

import resources.ResourcesLoader;
import resources.ThreadPDFLoader;
import view.LearningView;
import writer.Writer;

/**
 * Classe representant un perceptron multicouche
 *
 */
public class PMC {
	private SimpleMatrix W, Z;
	private DataManager data;
	private ConfusionMatrix matriceConfusion;
	private String path;
	private ArrayList<Sortie> sortie;
	private Boolean isUpdatingProgressBar = true;
	private int nombreNeuroneEntree, nombreNeuronesCC, nombreNeuroneSortie;
	private int nbStepMax = 200;
	private double learningSpeed = 0.002;
	private static int lenmat = 11;
	private static List<String> directoryName;

	/**
	 * Contructeur par defaut. Cree les differentes matrice et les initialise
	 * 
	 * @param k Paramètre pour la cross validation - 1/k correspond à la proportion de donnée utilisé pour le test
	 * @param neuronesCaches Nombre de neurones dans la couche cachee
	 * @param max Nombre maximum d'itérations
	 * @param lenMatrix taille du vecteur de features
	 * @param ls Vitesse d'apprentissage
	 * @param Path Chemin des dossier à récupérer
	 */
	public PMC(String Path, int k, int neuronesCaches, int max, int lenMatrix, double ls) {
		this.nbStepMax = max;
		this.path = Path;
		this.learningSpeed = ls;
		this.lenmat = lenMatrix + 1;
		
		this.data = new DataManager(Path);
		this.data.kfoldCrossValidation(k);
		this.matriceConfusion = new ConfusionMatrix(data.numClasses());
		
		this.nombreNeuronesCC = neuronesCaches;
		this.nombreNeuroneEntree = lenmat * 4+1;
		this.nombreNeuroneSortie = data.numClasses();

		Random rand = new Random();
		this.W = SimpleMatrix.random(nombreNeuronesCC, nombreNeuroneEntree, -1, 1, rand);
		this.Z = SimpleMatrix.random(nombreNeuroneSortie, nombreNeuronesCC, -1, 1, rand);
		
		this.directoryName = ResourcesLoader.getDirectoriesName();
		this.directoryName.remove("_IGNORE");
		
		StringBuilder directoryString = new StringBuilder("");
		
		for(String s:this.directoryName) {
			directoryString.append(s + "\n");
		}
		
		Writer.writeTo(directoryString.toString(), Const.MainPath + "directoryName.txt");
	}
	
	/**
	 * Contructeur d'un PMC si l'apprentissage n'est pas necessaire
	 * @param path Chemin des pdf
	 */
	public PMC(String path) {
		this.path = path;
		ResourcesLoader.loadResourcesIn(path);
		directoryName = ResourcesLoader.readFile(Const.MainPath + "directoryName.txt");
		loadWeightMatrix();
	}

	/**
	 * Calcule le leaky ReLU d'un double
	 * 
	 * @param x
	 * @return ReLU de x
	 */
	public static double relu(double x) {
		if(x < 0)
			return 0.01*x;
		else
			return x;
	}

	/**
	 * Calcule le leaky ReLU pour chaque élément de la matrice
	 * 
	 * @param X matrice d'entrée
	 * @return matrice de la taille de X
	 */
	public static SimpleMatrix relu(SimpleMatrix X) {
		SimpleMatrix res = new SimpleMatrix(X);

		for (int i = 0; i < res.numRows(); i++) {
			for (int j = 0; j < res.numCols(); j++) {
				res.set(i, j, relu(res.get(i, j)));
			}
		}

		return res;
	}

	/**
	 * Retourne la valeur absolue de la matrice d'entrée
	 * 
	 * @param X matrice d'entrée
	 * @return matrice de la taille de X
	 */
	public static SimpleMatrix abs(SimpleMatrix X) {
		SimpleMatrix res = new SimpleMatrix(X);

		for (int i = 0; i < res.numRows(); i++) {
			for (int j = 0; j < res.numCols(); j++) {
				res.set(i, j, Math.abs(res.get(i, j)));
			}
		}

		return res;
	}

	/**
	 * Calcule la dérivé de la fonction ReLU. Renvoie 1 en 0
	 * 
	 * @param x
	 * @return 0 si x < 0, 1 sinon
	 */
	public static double drelu(double x) {
		return x < 0 ? 0 : 1;
	}

	/**
	 * Calcule les poids en fonctions des échantillions d'apprentissage
	 * @param dataset Liste des samples correspondant aux pdf à apprendre
	 */
	private void learn(List<Sample> dataset) {
		Random rand = new Random();

		int nstep = 0;
		double epsilon = 1e-3, error = 1;

		W = SimpleMatrix.random(nombreNeuronesCC, nombreNeuroneEntree, -0.5, 0.5, rand);
		Z = SimpleMatrix.random(nombreNeuroneSortie, nombreNeuronesCC, -0.5, 0.5, rand);

		while (nstep < nbStepMax && error > epsilon) {
			error = 0;
			List<Sample> d = dataset;
			Collections.shuffle(d);
			
			for (int k = 0; k < dataset.size(); k++) {//201 x 5ms -> 1sec
				PDF currentPDF = ResourcesLoader.getPDFbyName((dataset.get(k).name));
				
				SimpleMatrix X = FeaturesToNeuron(currentPDF.getFeatures());
				X = X.divide(1000);
				X.set(4*lenmat, -1);
				SimpleMatrix T = getExpectedResultsMatrix(dataset.get(k));	
	
				SimpleMatrix Scouche = W.mult(X);
				SimpleMatrix S = Z.mult(relu(Scouche));
				
				// Correct W
				for (int i = 0; i < W.numRows(); i++) {
					for (int j = 0; j < W.numCols(); j++) {
						double s = 0;
						for (int m = 0; m < Z.numRows(); m++) {
							s += -2 * (T.get(m) - S.get(m)) * Z.get(m, i);
						}
						double dw = learningSpeed * drelu(Scouche.get(i)) * s * X.get(j);
						W.set(i, j, W.get(i, j) - dw);
					}
				}
				
				// Correct Z
				for (int i = 0; i < Z.numRows(); i++) {
					for (int j = 0; j < Z.numCols(); j++) {
						double dz = -2 * learningSpeed * (T.get(i) - S.get(i)) * relu(Scouche.get(j));
						Z.set(i, j, Z.get(i, j) - dz);
					}
				}

				error += abs(T.minus(S)).elementSum()/dataset.size();				
				
				if(Double.isNaN(error)) {
					System.out.println("Error is NaN ! Reset.");
					
					W = SimpleMatrix.random(nombreNeuronesCC, nombreNeuroneEntree, -0.5, 0.5, rand);
					Z = SimpleMatrix.random(nombreNeuroneSortie, nombreNeuronesCC, -0.5, 0.5, rand);
										
					nstep = 0;
					k = 0;
					error = 1;
				}
			}
			nstep++;			
			
			if(isUpdatingProgressBar)
				LearningView.incrementProgressBar();
		}
	}

	/**
	 * calcule le score pour chaque partern en fonction du pdf d'entrée
	 * @param pdf PDF à calculer
	 * @return index du meilleur pattern
	 */
	public int compute(PDF pdf) {
		SimpleMatrix X = FeaturesToNeuron(pdf.getFeatures());
		X = X.divide(1000);
		X.set(4*lenmat, -1);
		SimpleMatrix S = Z.mult(relu(W.mult(X)));

		int indMaxi = 0;
		double maxi = S.get(0, 0);
		
		sortie = new ArrayList<Sortie>();
		
		for (int i = 0; i < S.numRows(); i++) {
			sortie.add(new Sortie(directoryName.get(i), S.get(i)));
			if (S.get(i) > maxi) {
				maxi = S.get(i);
				indMaxi = i;
			}
		}
		
		Collections.sort(sortie);
		Collections.reverse(sortie);
	
		return indMaxi;
	}
	
	/**
	 * Renvoie la liste des sorties
	 * @return liste de sortie
	 */
	public List<Sortie> getSortie() {
		return sortie;
	}

	/**
	 * Effectue l'apprentissage suivant la méthode k-fold cross-validation
	 */
	public void learnAndTest() {
		matriceConfusion.reset();
		for (int currentTest = 0; currentTest < data.getK(); currentTest++) {

			ArrayList<Sample> learningData = new ArrayList<Sample>();

			for (int i = 0; i < data.getK(); i++) {
				if (i != currentTest)
					learningData.addAll(data.getData().get(i));
			}

			learn(learningData);

			for (Sample s : data.getData().get(currentTest)) {
				int res = compute(ResourcesLoader.getPDFbyName(s.name));
				matriceConfusion.increment(s.number, res);
			}
		}
		matriceConfusion.computeStats();

		saveWeightMatrix();
	}
	
	/**
	 * Version threadé de l'apprentissage suivant la méthode k-fold cross-validation
	 */
	public void learnAndTestThread() {
		matriceConfusion.reset();
		ExecutorService service = Executors.newFixedThreadPool(data.getK());
		for (int currentTest = 0; currentTest < data.getK(); currentTest++) {

			ArrayList<Sample> learningData = new ArrayList<Sample>();

			for (int i = 0; i < data.getK(); i++) {
				if (i != currentTest)
					learningData.addAll(data.getData().get(i));
			}
			service.execute(new ThreadLearningTesting(learningData, nombreNeuroneEntree, nombreNeuronesCC, nombreNeuroneSortie, nbStepMax, lenmat, learningSpeed, isUpdatingProgressBar, matriceConfusion, currentTest, data));
		}
		ShutdownThreads.shutdownAndAwaitTermination(service, 10*60);
		
		matriceConfusion.computeStats();

		saveWeightMatrix();
	}

	/**
	 * Effectue l'apprentissage sur tous les pdf
	 */
	public void learnOnly() {	
		ArrayList<Sample> alldata = new ArrayList<Sample>();
		for (List<Sample> l : data.getData()) {
			alldata.addAll(l);
		}

		learn(alldata);
		
		saveWeightMatrix();
	}

	/**
	 * Transforme une liste de features en vecteur d'entrée
	 * @param Flist 
	 * @return Matrice exploitable pour les calculs
	 */
	static public SimpleMatrix FeaturesToNeuron(List<Feature> Flist) {
		SimpleMatrix output = new SimpleMatrix(lenmat * 4 + 1, 1);
		String cat = Flist.get(0).getType();
		if(cat == null) {
			System.out.println("");
		}
		int fInd = 0;
		for (int n = 0; n < 4; n++) {
			for (int j = 1 + lenmat * n; j < lenmat * (n + 1); j++) {
				if (fInd < Flist.size()) {
					if (!cat.equalsIgnoreCase(Flist.get(fInd).getType())) {
						output.set(j, -1);
					} else {
						output.set(n * lenmat, output.get(n * lenmat) + 1);
						output.set(j, Flist.get(fInd).getPos());
						fInd++;
					}
				}
			}
			if (fInd < Flist.size()) {
				if (n < 3) {
					while ((cat.equalsIgnoreCase(Flist.get(fInd).getType())) & (fInd < Flist.size() - 1)) {
						fInd++;
					}
				}
				if (fInd < Flist.size()) {
					cat = Flist.get(fInd).getType();
				}
			}
		}
		
		output.set(lenmat * 4, -1);
		return output;

	}

	/**
	 * Crée la matrice des resultats attendu en fonction de l'échantillion d'entrée 
	 * @param echantillon
	 * @return Matrice exploitable pour les calculs
	 */
	static public SimpleMatrix getExpectedResultsMatrix(Sample echantillon) {
		
		SimpleMatrix expectedResult = new SimpleMatrix(directoryName.size(), 1);
		if (echantillon.number != -1) {
			expectedResult.set(echantillon.number, 1);
		}
		return expectedResult;
	}
	
	/**
	 * Charge les matrices de poids
	 */
	@SuppressWarnings("static-access")
	public void loadWeightMatrix() {
		try {
			W=W.loadCSV(Const.MainPath + "\\weightMatrixW.csv");
			Z=Z.loadCSV(Const.MainPath + "\\weightMatrixZ.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Sauvegarde les matrices de poids
	 */
	public void saveWeightMatrix() {
		try {
			W.saveToFileCSV(Const.MainPath + "\\weightMatrixW.csv");
			Z.saveToFileCSV(Const.MainPath + "\\weightMatrixZ.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ConfusionMatrix getConfusionMatrix() {
		return matriceConfusion;
	}
	
	public SimpleMatrix getW() {
		return W;
	}

	public void setW(SimpleMatrix WM) {
		W=WM;
	}
	
	public void setUpdating(Boolean val) {
		isUpdatingProgressBar = val;
	}
}
