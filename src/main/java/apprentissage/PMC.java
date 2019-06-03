package apprentissage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;

import com.sun.glass.ui.Size;

import resources.ResourcesLoader;

/**
 * Classe representant un perceptron multicouche
 *
 */
public class PMC {
	SimpleMatrix S, R, X, W, Z;
	int nombreNeuroneEntree, nombreNeuronesCC, nombreNeuroneSortie;
	
	/**
	 * Contructeur par defaut. Cree les differentes matrice et les initialise
	 * @param nbEntree Nombre de neuronnes d'entree
	 * @param nbCC Nombre de neuronne dans la couche cachee
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
	
	public static SimpleMatrix getExpectedResultsMatrix(Sample echantillon) {
		String root = "src/main/resources/pdf";

		List<String> directoryName = ResourcesLoader.getDirectoriesName(root);
		directoryName.remove("_IGNORE");

		ArrayList<Sample> samplePattern = new ArrayList<Sample>(directoryName.size());
		
		SimpleMatrix expectedResult= new SimpleMatrix(directoryName.size(),1);
		if (echantillon.number!=-1) {
			expectedResult.set(echantillon.number, 1);
		}
		
		return expectedResult;
		
	}
}
