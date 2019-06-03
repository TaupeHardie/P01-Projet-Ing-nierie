package apprentissage;

import java.util.Random;

import org.ejml.simple.SimpleMatrix;

/**
 * Classe représentant un perceptron multicouche
 *
 */
public class PMC {
	SimpleMatrix S, R, X, W, Z;
	int nombreNeuroneEntree, nombreNeuronesCC, nombreNeuroneSortie;
	
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
		
		X = new SimpleMatrix(nombreNeuroneEntree, 10);
		
		W = SimpleMatrix.random(nombreNeuronesCC, nombreNeuroneEntree, -1, 1, rand);
		Z = SimpleMatrix.random(nombreNeuroneSortie, nombreNeuronesCC, -1, 1, rand);
	}
}
