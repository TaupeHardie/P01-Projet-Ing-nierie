package apprentissage;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;

import misc.PDF;
import resources.ResourcesLoader;
import view.LearningView;

/**
 * This class is used to compute one cross-validation 
 * see PMC.learn method
 * @author axel
 */
public class ThreadLearningTesting implements Runnable{
	private final int nbStepMax = 200;
	private final static int lenmat = 11;
	private SimpleMatrix W, Z;
	
	
	private List<Sample> dataset;
	private int nombreNeuroneEntree, nombreNeuronesCC, nombreNeuroneSortie;
	private ConfusionMatrix matriceConfusion;
	private int currentTest;
	private DataManager data;
	
	
	
	public ThreadLearningTesting(List<Sample> dataset, int nombreNeuroneEntree, int nombreNeuronesCC,
			int nombreNeuroneSortie, ConfusionMatrix matriceConfusion, int currentTest, DataManager data) {
		super();
		this.dataset = dataset;
		this.nombreNeuroneEntree = nombreNeuroneEntree;
		this.nombreNeuronesCC = nombreNeuronesCC;
		this.nombreNeuroneSortie = nombreNeuroneSortie;
		this.matriceConfusion = matriceConfusion;
		this.currentTest = currentTest;
		this.data = data;
	}

	@Override
	public void run() {
		Random rand = new Random();

		double l = 0.002;
		int nstep = 0;
		double epsilon = 1e-3, error = 1;

		W = SimpleMatrix.random(nombreNeuronesCC, nombreNeuroneEntree, -0.5, 0.5, rand);
		Z = SimpleMatrix.random(nombreNeuroneSortie, nombreNeuronesCC, -0.5, 0.5, rand);

		while (nstep < nbStepMax && error > epsilon) {
			error = 0;
			Collections.shuffle(dataset);
			
			for (int k = 0; k < dataset.size(); k++) {//201 x 5ms -> 1sec
				PDF currentPDF = ResourcesLoader.getPDFbyName((dataset.get(k).name));
				
				SimpleMatrix X = PMC.FeaturesToNeuron(currentPDF.getFeatures());
				X = X.divide(1000);
				X.set(4*lenmat, -1);
				SimpleMatrix T = PMC.getExpectedResultsMatrix(dataset.get(k));	
	
				SimpleMatrix Scouche = W.mult(X);
				SimpleMatrix S = Z.mult(PMC.relu(Scouche));
				
				// Correct W
				for (int i = 0; i < W.numRows(); i++) {
					for (int j = 0; j < W.numCols(); j++) {
						double s = 0;
						for (int m = 0; m < Z.numRows(); m++) {
							s += -2 * (T.get(m) - S.get(m)) * Z.get(m, i);
						}
						double dw = l * PMC.drelu(Scouche.get(i)) * s * X.get(j);
						W.set(i, j, W.get(i, j) - dw);
					}
				}
				
				// Correct Z
				for (int i = 0; i < Z.numRows(); i++) {
					for (int j = 0; j < Z.numCols(); j++) {
						double dz = -2 * l * (T.get(i) - S.get(i)) * PMC.relu(Scouche.get(j));
						Z.set(i, j, Z.get(i, j) - dz);
					}
				}

				error += PMC.abs(T.minus(S)).elementSum()/dataset.size();				
				
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
			LearningView.incrementProgressBar();
			System.out.println("Step : " + nstep + "/" + nbStepMax + " (" + error +")");
		}
		
		for (Sample s : data.getData().get(currentTest)) {
			int res = compute(s.name);
			matriceConfusion.increment(s.number, res);
		}	
	}
	
	/**
	 * same compute method as in PMC.compute
	 * @param filename 
	 * @return
	 */
	private int compute(String filename) {
		PDF p = ResourcesLoader.getPDFbyName(filename);

		SimpleMatrix X = PMC.FeaturesToNeuron(p.getFeatures());
		SimpleMatrix S = Z.mult(PMC.relu(W.mult(X)));

		int indMaxi = 0;
		double maxi = S.get(0, 0);
		for (int i = 1; i < S.numRows(); i++) {
			if (S.get(i) > maxi) {
				maxi = S.get(i);
				indMaxi = i;
			}
		}
		return indMaxi;
	}
}
