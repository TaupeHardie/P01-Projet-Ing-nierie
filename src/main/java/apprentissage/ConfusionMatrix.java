package apprentissage;

import org.ejml.simple.SimpleMatrix;

/**
 * Classe gerant la matrice de confusion
 *
 */
public class ConfusionMatrix {
	
	SimpleMatrix confMatrix;
	int classNb;
	double rappel, precision;
	
	/**
	 * Constructeur initialisant la matrice de confusion a 0
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
	 * calcul la precision et le rappel de la matrice de confusion
	 */
	public void computeStats() {
		rappel = 0;
		for (int i = 0; i<classNb;i++) {
			double ligne=0;
			for (int j=0;j<classNb;j++) {
				ligne+=confMatrix.get(i,j);
			}
			rappel+=confMatrix.get(i, i)/ligne;
		}
		rappel/=classNb;
		
		for (int i = 0; i<classNb;i++) {
			double col=0;
			for (int j=0;j<classNb;j++) {
				col+=confMatrix.get(j,i);
			}
			precision+=confMatrix.get(i, i)/col;
		}
		precision/=classNb;
	}
	
	/**
	 * renvoie le rappel
	 * @return double rappel
	 */
	public double getRappel() {
		return rappel;
	}
	
	/**
	 * renvoie la precision 
	 * @return double precision
	 */
	public double getPrecision() {
		return precision;
	}
	
	
	/**
	 * reinitialise la matrice de confusion
	 */
	
	public void reset() {
		confMatrix=new SimpleMatrix(classNb,classNb);
	}
	
}
