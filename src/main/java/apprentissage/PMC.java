package apprentissage;

import java.util.Random;

import org.ejml.simple.SimpleMatrix;

/**
 * Classe représentant un perceptron multicouche
 *
 */
public class PMC {
	private SimpleMatrix T, X, W, Z;
	private int nombreNeuroneEntree, nombreNeuronesCC, nombreNeuroneSortie;
	private final int nbStepMax = 1000;
	
	/**
	 * Contructeur par défaut. Crée les différentes matrice et les initialise
	 * @param nbEntree Nombre de neuronnes d'entrée
	 * @param nbCC Nombre de neuronne dans la couche cahcée
	 * @param nbSortie Nombre de neurone dans la couche de sortie
	 */
	public PMC(int nbEntree, int nbCC, int nbSortie) {
		Random rand = new Random();
		
		nombreNeuronesCC = nbCC;
		nombreNeuroneEntree = nbEntree;
		nombreNeuroneSortie = nbSortie;
		
		X = new SimpleMatrix(nombreNeuroneEntree, 1);
		
		T = new SimpleMatrix(nombreNeuroneSortie, 1);
		
		W = SimpleMatrix.random(nombreNeuronesCC, nombreNeuroneEntree, -1, 1, rand);
		Z = SimpleMatrix.random(nombreNeuroneSortie, nombreNeuronesCC, -1, 1, rand);
	}
	
	/**
	 * Calcule le ReLU d'un double
	 * @param x
	 * @return ReLU de x
	 */
	private double relu(double x) {
		return Math.max(0, x);
	}
	
	/**
	 * Calcule le ReLU pour chaque élément de la matrice
	 * @param X matrice d'entrée
	 * @return matrice de la taille de X
	 */
	private SimpleMatrix relu(SimpleMatrix X) {
		SimpleMatrix res = new SimpleMatrix(X);
		
		for(int i = 0; i < res.numRows(); i++) {
			for(int j = 0; j < res.numCols(); j++) {
				res.set(i, j, relu(res.get(i, j)));
			}
		}
		
		return res;
	}
	
	/**
	 * Retourne la valeur absolue de la matrice d'entrée
	 * @param X matrice d'entrée
	 * @return matrice de la taille de X
	 */
	private SimpleMatrix abs(SimpleMatrix X) {
		SimpleMatrix res = new SimpleMatrix(X);
		
		for(int i = 0; i < res.numRows(); i++) {
			for(int j = 0; j < res.numCols(); j++) {
				res.set(i, j, Math.abs(res.get(i, j)));
			}
		}
		
		return res;
	}
	
	/**
	 * Calcule la dérivé de la fonction ReLU. Renvoie 1 en 0
	 * @param x 
	 * @return 0 si x < 0, 1 sinon
	 */
	private double drelu(double x) {
		return x < 0 ? 0:1;
	}
	
	/**
	 * Calcule les poids en fonctions des échantillions d'apprentissage
	 */
	public void learn() {
		
		double l = 0.01;
		int nstep = 0;
		double epsilon = 1e-5, error = 1;
		
		while(nstep < nbStepMax && error > epsilon) {
		
			SimpleMatrix Scouche = W.mult(X);
			SimpleMatrix S = Z.mult(relu(Scouche));
			
			// Correct W
			for(int i = 0; i < W.numRows(); i++) {
				for(int j = 0; j < W.numCols(); j++) {
					double s = 0;
					for(int m = 0; m < Z.numRows(); m++) {
						s+= -2*(T.get(m) - S.get(m))*Z.get(m, j);
					}
					double dw = l*drelu(Scouche.get(j))*s;
					W.set(i, j, W.get(i,j) * dw);
				}
			}
			
			// Correct Z
			for(int i = 0; i < Z.numRows(); i++) {
				for(int j = 0; j < Z.numCols(); j++) {
					double dz = -2*l*(T.get(i) - S.get(i))*relu(Scouche.get(j));
					Z.set(i, j, Z.get(i,j)*dz);
				}
			}
			
			error = abs(T.minus(S)).elementSum();
			nstep++;
		}
	}
}