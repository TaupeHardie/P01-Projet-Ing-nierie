package apprentissage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	private SimpleMatrix T, X, W, Z;
	private DataManager data;
	private int nombreNeuroneEntree, nombreNeuronesCC, nombreNeuroneSortie;
	private final int nbStepMax = 1000;
	final static int lenmat = 11;

	/**
	 * Contructeur par defaut. Cree les differentes matrice et les initialise
	 * 
	 * @param nbEntree Nombre de neuronnes d'entree
	 * @param nbCC     Nombre de neuronne dans la couche cachee
	 * @param nbSortie Nombre de neurone dans la couche de sortie
	 */
	public PMC(int k, int neuronesCaches) {
		Random rand = new Random();
		data = new DataManager();
		data.kfoldCrossValidation(k);

		nombreNeuronesCC = neuronesCaches;
		nombreNeuroneEntree = lenmat * 4;
		nombreNeuroneSortie = data.numClasses();

		W = SimpleMatrix.random(nombreNeuronesCC, nombreNeuroneEntree, -1, 1, rand);
		Z = SimpleMatrix.random(nombreNeuroneSortie, nombreNeuronesCC, -1, 1, rand);
	}

	/**
	 * Calcule le ReLU d'un double
	 * 
	 * @param x
	 * @return ReLU de x
	 */
	private double relu(double x) {
		return Math.max(0, x);
	}

	/**
	 * Calcule le ReLU pour chaque élément de la matrice
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
	private void learn(List<Sample> dataset) {
		Random rand = new Random();

		double l = 0.01;
		int nstep = 0;
		double epsilon = 1e-5, error = 1;

		W = SimpleMatrix.random(nombreNeuronesCC, nombreNeuroneEntree, -1, 1, rand);
		Z = SimpleMatrix.random(nombreNeuroneSortie, nombreNeuronesCC, -1, 1, rand);

		while (nstep < nbStepMax && error > epsilon) {
			error = 0;
			for (int k = 0; k < dataset.size(); k++) {

				PDF currentPDF = new PDF(dataset.get(k).name);
				currentPDF.convertToText();

				SimpleMatrix X = FeaturesToNeuron(currentPDF.findMatches());
				T = getExpectedResultsMatrix(dataset.get(k));

				SimpleMatrix Scouche = W.mult(X.transpose());
				SimpleMatrix S = Z.mult(relu(Scouche));

				// Correct W
				for (int i = 0; i < W.numRows(); i++) {
					for (int j = 0; j < W.numCols(); j++) {
						double s = 0;
						for (int m = 0; m < Z.numRows(); m++) {
							s += -2 * (T.get(m) - S.get(m)) * Z.get(m, i);
						}
						double dw = l * drelu(Scouche.get(i)) * s;
						W.set(i, j, W.get(i, j) * dw);
					}
				}

				// Correct Z
				for (int i = 0; i < Z.numRows(); i++) {
					for (int j = 0; j < Z.numCols(); j++) {
						double dz = -2 * l * (T.get(i) - S.get(i)) * relu(Scouche.get(j));
						Z.set(i, j, Z.get(i, j) * dz);
					}
				}

				error += abs(T.minus(S)).elementSum();
			}
			nstep++;
		}
	}

	public int compute(String filename) {
		PDF pdf = new PDF(filename);
		pdf.convertToText();

		SimpleMatrix X = FeaturesToNeuron(pdf.findMatches());
		SimpleMatrix S = Z.mult(relu(W.mult(X)));

		int indMaxi = 0;
		double maxi = S.get(0, 0);
		for (int i = 1; i < S.numRows(); i++) {
			if (S.get(i, 0) > maxi) {
				maxi = S.get(i, 0);
				indMaxi = i;
			}
		}

		return indMaxi;
	}

	public void learnAndTest() {
		for (int currentTest = 0; currentTest < data.getK(); currentTest++) {
			ArrayList<Sample> learningData = new ArrayList<Sample>();

			for (int i = 0; i < data.getK(); i++) {
				if (i != currentTest)
					learningData.addAll(data.getData().get(i));
			}

			learn(learningData);

			for (Sample s : data.getData().get(currentTest)) {
				int res = compute(s.name);
			}
		}
	}

	public void learnOnly() {
		ArrayList<Sample> alldata = new ArrayList<Sample>();
		for (List<Sample> l : data.getData()) {
			alldata.addAll(l);
		}

		learn(alldata);
	}
	/**
	 * Transforme une liste de feature en une matrice qui sera utilisable pour le reseau de neurones
	 * @param Flist
	 * @return SimpleMatrix output
	 */
	static public SimpleMatrix FeaturesToNeuron(List<Feature> Flist) {
		SimpleMatrix output = new SimpleMatrix(1, lenmat * 4);
		String cat = Flist.get(0).getType();
		int fInd = 0;
		for (int n = 0; n < 4; n++) {
			for (int j = 1 + lenmat * n; j < lenmat * (n + 1); j++) {
				if (fInd < Flist.size()) {
					if (cat != Flist.get(fInd).getType()) {
						output.set(0, j, -1);
					} else {
						output.set(0, n * lenmat, output.get(0, n * lenmat) + 1);
						output.set(0, j, Flist.get(fInd).getPos());
						fInd++;
					}
				}
			}
			if (fInd < Flist.size()) {
				if (n < 3) {
					while ((cat == Flist.get(fInd).getType()) & (fInd < Flist.size() - 1)) {
						fInd++;
					}
				}
				if (fInd < Flist.size()) {
					cat = Flist.get(fInd).getType();
				}
			}
		}
		return output;

	}
	
	/**
	 * calcul et renvoie une matrice de taille (Nb de classe * 1) correspondant a la matrice des 
	 * resultats attendus pour un echantillon
	 * @param echantillon
	 * @return SimpleMatrix expectedResult
	 */

	static public SimpleMatrix getExpectedResultsMatrix(Sample echantillon) {
		String root = "src/main/resources/pdf";

		List<String> directoryName = ResourcesLoader.getDirectoriesName(root);
		directoryName.remove("_IGNORE");

		SimpleMatrix expectedResult = new SimpleMatrix(directoryName.size(), 1);
		if (echantillon.number != -1) {
			expectedResult.set(echantillon.number, 1);
		}

		return expectedResult;

	}
}
