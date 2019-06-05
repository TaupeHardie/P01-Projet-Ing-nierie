package apprentissage;

import org.ejml.simple.SimpleMatrix;

public class ConfusionMatrix {
	
	SimpleMatrix confMatrix;
	int classNb;
	double rappel, precision;
	
	public ConfusionMatrix(int numbOfClasses) {
		this.confMatrix=new SimpleMatrix(numbOfClasses,numbOfClasses);
		classNb=numbOfClasses;
	}
	
	public void increment(int expected, int result) {
		confMatrix.set(expected, result, confMatrix.get(expected, result)+1);
	}
	
	public void computeStats() {
		rappel = 0;
		for (int i = 0; i<classNb;i++) {
			double ligne=0;
			for (int j=0;j<classNb;j++) {
				ligne+=confMatrix.get(i,j);
			}
			if(ligne != 0)
				rappel+=confMatrix.get(i, i)/ligne;
		}
		rappel/=classNb;
		
		for (int i = 0; i<classNb;i++) {
			double col=0;
			for (int j=0;j<classNb;j++) {
				col+=confMatrix.get(j,i);
			}
			if(col != 0)
				precision+=confMatrix.get(i, i)/col;
		}
		precision/=classNb;
	}
	
	public double getRappel() {
		return rappel;
	}
	
	public double getPrecision() {
		return precision;
	}
	
	public void reset() {
		confMatrix=new SimpleMatrix(classNb,classNb);
	}
	
}
