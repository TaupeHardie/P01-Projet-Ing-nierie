package apprentissage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.ejml.simple.SimpleMatrix;

import extraction.Feature;
import main.PDF;

import com.sun.glass.ui.Size;

import resources.ResourcesLoader;

/**
 * Classe representant un perceptron multicouche
 *
 */
public class PMC {
	private SimpleMatrix W, Z;
	private DataManager data;
	private ConfusionMatrix matriceConfusion;
	private int nombreNeuroneEntree, nombreNeuronesCC, nombreNeuroneSortie;
	private final int nbStepMax = 200;
	final static int lenmat = 11;

	/**
	 * Contructeur par defaut. Cree les differentes matrice et les initialise
	 * 
	 * @param nbEntree Nombre de neuronnes d'entree
	 * @param nbCC     Nombre de neuronne dans la couche cachee
	 * @param nbSortie Nombre de neurone dans la couche de sortie
	 */
	public PMC(int k, int neuronesCaches, String Path) {
		Random rand = new Random();
		data = new DataManager();
		data.kfoldCrossValidation(k,Path);
		matriceConfusion = new ConfusionMatrix(data.numClasses());
		
		nombreNeuronesCC = neuronesCaches;
		nombreNeuroneEntree = lenmat * 4+1;
		nombreNeuroneSortie = data.numClasses();

		W = SimpleMatrix.random(nombreNeuronesCC, nombreNeuroneEntree, -1, 1, rand);
		Z = SimpleMatrix.random(nombreNeuroneSortie, nombreNeuronesCC, -1, 1, rand);
	}

	/**
	 * Calcule le leaky ReLU d'un double
	 * 
	 * @param x
	 * @return ReLU de x
	 */
	private double relu(double x) {
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
	private SimpleMatrix relu(SimpleMatrix X) {
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
	private SimpleMatrix abs(SimpleMatrix X) {
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
	private double drelu(double x) {
		return x < 0 ? 0 : 1;
	}

	/**
	 * Calcule les poids en fonctions des échantillions d'apprentissage
	 */
	private void learn(List<Sample> dataset, String path) {
		Random rand = new Random();

		double l = 0.001;
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
				SimpleMatrix T = getExpectedResultsMatrix(dataset.get(k), path);	
	
				SimpleMatrix Scouche = W.mult(X);
				SimpleMatrix S = Z.mult(relu(Scouche));
				
				// Correct W
				for (int i = 0; i < W.numRows(); i++) {
					for (int j = 0; j < W.numCols(); j++) {
						double s = 0;
						for (int m = 0; m < Z.numRows(); m++) {
							s += -2 * (T.get(m) - S.get(m)) * Z.get(m, i);
						}
						double dw = l * drelu(Scouche.get(i)) * s * X.get(j);
						W.set(i, j, W.get(i, j) - dw);
					}
				}
				
				// Correct Z
				for (int i = 0; i < Z.numRows(); i++) {
					for (int j = 0; j < Z.numCols(); j++) {
						double dz = -2 * l * (T.get(i) - S.get(i)) * relu(Scouche.get(j));
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
				
				if(k == 0 && nstep == nbStepMax - 1) {
					for(int i = 0; i < T.numRows(); i++) {
						System.out.println(T.get(i) + " " + S.get(i));
					}						
				}
			}
			nstep++;			
			System.out.println("Step : " + nstep + "/" + nbStepMax + " (" + error +")");
		}
	}

	public int compute(String filename) {
		PDF pdf = new PDF(filename);

		SimpleMatrix X = FeaturesToNeuron(pdf.getFeatures());
		SimpleMatrix S = Z.mult(relu(W.mult(X)));

		int indMaxi = 0;
		double maxi = S.get(0, 0);
		for (int i = 1; i < S.numRows(); i++) {
			if (S.get(i) > maxi) {
				maxi = S.get(i);
				indMaxi = i;
			}
		}
		
		pdf.close();
		return indMaxi;
	}

	public void learnAndTest(String path) {
		
		long t = System.nanoTime();
		List<File> files = ResourcesLoader.loadDirectory(path);
		ResourcesLoader.loadAllPdf(files);
		System.out.println("load all pdf time : "+(System.nanoTime() - t)/1000000000);
		
		matriceConfusion.reset();
		for (int currentTest = 0; currentTest < data.getK(); currentTest++) {
			long t1 = System.nanoTime();
			System.out.println("------------------------");
			System.out.println("Kfold : " + (currentTest+1) + "/" + data.getK());
			ArrayList<Sample> learningData = new ArrayList<Sample>();

			for (int i = 0; i < data.getK(); i++) {
				if (i != currentTest)
					learningData.addAll(data.getData().get(i));
			}

			System.out.println("Size : " + learningData.size());
			learn(learningData, path);

			for (Sample s : data.getData().get(currentTest)) {
				int res = compute(s.name);
				matriceConfusion.increment(s.number, res);
			}
			long t2 = System.nanoTime();
			matriceConfusion.computeStats();
			System.out.println((currentTest+1)+"e confus time : "+(System.nanoTime() - t2)/1000000000 +"s");
			System.out.println("Rappel :" + matriceConfusion.getRappel());
			System.out.println("Precision : " + matriceConfusion.getPrecision());
			System.out.println((currentTest+1)+"e kfold time : "+(System.nanoTime() - t1)/1000000000 +"s");
		}
		saveWeightMatrix();
	}

	public void learnOnly(String path) {	
		long t = System.nanoTime();
		List<File> files = ResourcesLoader.loadDirectory(path);
		ResourcesLoader.loadAllPdf(files);
		System.out.println("load all pdf time : "+(System.nanoTime() - t)/1000000000);
		
		ArrayList<Sample> alldata = new ArrayList<Sample>();
		for (List<Sample> l : data.getData()) {
			alldata.addAll(l);
		}

		learn(alldata, path);
	}

	static public SimpleMatrix FeaturesToNeuron(List<Feature> Flist) {
		SimpleMatrix output = new SimpleMatrix(lenmat * 4 + 1, 1);
		String cat = Flist.get(0).getType();
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

	static public SimpleMatrix getExpectedResultsMatrix(Sample echantillon, String path) {

		List<String> directoryName = ResourcesLoader.getDirectoriesName(path);
		directoryName.remove("_IGNORE");

		SimpleMatrix expectedResult = new SimpleMatrix(directoryName.size(), 1);
		if (echantillon.number != -1) {
			expectedResult.set(echantillon.number, 1);
		}

		return expectedResult;

	}
	
	@SuppressWarnings("static-access")
	public void loadWeightMatrix() {
		try {
			W.loadCSV("weightMatrix.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveWeightMatrix() {
		try {
			W.saveToFileCSV("weightMatrix.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
