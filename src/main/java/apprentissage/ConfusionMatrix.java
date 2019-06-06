package apprentissage;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

import resources.ResourcesLoader;

public class ConfusionMatrix {
	
	SimpleMatrix confMatrix;
	String path;
	int classNb;
	double rappel, precision;
	
	public ConfusionMatrix(int numbOfClasses, String path) {
		this.confMatrix=new SimpleMatrix(numbOfClasses,numbOfClasses);
		this.path = path;
		classNb=numbOfClasses;
	}
	
	public synchronized void increment(int expected, int result) {
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
	
	@Override
	public String toString() {
		//custom toString to print the header (name of the class) of the table
		List<String> dirNames = ResourcesLoader.getDirectoriesName(path);
		dirNames.remove("_IGNORE");
		StringBuilder rtn = new StringBuilder();
		
		for(String dirName : dirNames) {
			rtn.append(";"+dirName);
		}
		rtn.append("\n");
		for(int l =0; l< confMatrix.numRows();l++) {
			rtn.append(dirNames.get(l));
			for(int c =0; c< confMatrix.numCols();c++) {
				rtn.append(";"+confMatrix.get(l, c));
			}
			rtn.append("\n");
		}
		return rtn.toString();
	}
	
}
