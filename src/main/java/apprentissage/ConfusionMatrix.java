package apprentissage;

import org.ejml.simple.SimpleMatrix;

/**
 * Classe gerant la matrice de confusion
 *
 */
public class ConfusionMatrix {
	
	SimpleMatrix confMatrix;
	int classNb;
	
	/**
	 * Constructeur initialisant la matrice de confusion à 0
	 * @param numbOfClasses
	 */
	
	public ConfusionMatrix(int numbOfClasses) {
		this.confMatrix=new SimpleMatrix(numbOfClasses,numbOfClasses);
		classNb=numbOfClasses;
	}
	
	/**
	 * Fonction permettant d'incrementer la matrice de confusion: prend en parametre 
	 * l'indice de la classe attendu et de la classe obtenu en resultat et ajoute +1 
	 *  la case de la matrice de confusion concerne
	 * @param expected
	 * @param result
	 */
	public void increment(int expected, int result) {
		confMatrix.set(expected, result, confMatrix.get(expected, result)+1);
	}
	
	/**
	 * calcul et renvoie le rappel de la matrice de confusion
	 * @return double rappel
	 */
	
	public double getRappel() {
		double rappel=0;
		for (int i = 0; i<classNb;i++) {
			double ligne=0;
			for (int j=0;j<classNb;j++) {
				ligne+=confMatrix.get(i,j);
			}
			rappel+=confMatrix.get(i, i)/ligne;
		}
		return rappel/classNb;
	}
	
	/**
	 * calcul et renvoie la precision de la matrice de confusion
	 * @return precision
	 */
	
	public double getPrecision() {
		double precision=0;
		for (int i = 0; i<classNb;i++) {
			double col=0;
			for (int j=0;j<classNb;j++) {
				col+=confMatrix.get(j,i);
			}
			precision+=confMatrix.get(i, i)/col;
		}
		return precision/classNb;
	}
	
	/**
	 * reinitialise la matrice de confusion
	 */
	
	public void reset() {
		confMatrix=new SimpleMatrix(classNb,classNb);
	}
	
}
