package apprentissage;

import org.ejml.simple.SimpleMatrix;

public class ConfusionMatrix {
	
	SimpleMatrix confMatrix;
	int classNb;
	
	public ConfusionMatrix(int numbOfClasses) {
		this.confMatrix=new SimpleMatrix(numbOfClasses,numbOfClasses);
		classNb=numbOfClasses;
	}
	
	public void increment(int expected, int result) {
		confMatrix.set(expected, result, confMatrix.get(expected, result)+1);
	}
	
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
	
	public void reset() {
		confMatrix=new SimpleMatrix(classNb,classNb);
	}
	
}
