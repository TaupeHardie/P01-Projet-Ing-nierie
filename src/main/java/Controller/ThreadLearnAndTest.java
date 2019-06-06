package Controller;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import apprentissage.ConfusionMatrix;
import apprentissage.PMC;
import resources.ResourcesLoader;

public class ThreadLearnAndTest implements Callable<ConfusionMatrix>{
	
	private int k, nbCoucheCachee, nbSteps, lenMatrix;
	private double learningSpeed;
	String path;

	public ThreadLearnAndTest(String path, int k, int nbCoucheCachee, int nbSteps, int lenMatrix, double learningSpeed) {
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
		List<File> files = ResourcesLoader.loadDirectory(path);
		ResourcesLoader.loadAllPdf(files);
		
		PMC pmc = new PMC(path, k, nbCoucheCachee, nbSteps, lenMatrix, learningSpeed);
		pmc.learnAndTest();
		
		return pmc.getConfusionMatrix();
	}

}
