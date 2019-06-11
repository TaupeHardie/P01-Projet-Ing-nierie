package Controller;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import apprentissage.ConfusionMatrix;
import apprentissage.PMC;
import resources.ResourcesLoader;


public class ThreadlessLearnAndTest implements Callable<ConfusionMatrix>{
	
	private int k, nbCoucheCachee, nbSteps, lenMatrix;
	private double learningSpeed;
	String path;

	public ThreadlessLearnAndTest(String path, int k, int nbCoucheCachee, int nbSteps, int lenMatrix, double learningSpeed) {
		super();
		this.k = k;
		this.nbCoucheCachee = nbCoucheCachee;
		this.nbSteps = nbSteps;
		this.path = path;
		this.lenMatrix = lenMatrix;
		this.learningSpeed = learningSpeed;
		
		if(learningSpeed == 0)
			this.learningSpeed = 0.001;
	}

	@Override
	public ConfusionMatrix call() throws Exception {
    	long t1 = System.nanoTime();
		PMC pmc = new PMC(path, k, nbCoucheCachee, nbSteps, lenMatrix, learningSpeed);
		pmc.learnAndTest();
		System.out.println("learning ended in : "+(System.nanoTime()-t1)/1000000000+"s");
		
		return pmc.getConfusionMatrix();
	}

}
