package Controller;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import apprentissage.ConfusionMatrix;
import apprentissage.PMC;
import resources.ResourcesLoader;
import view.LearningView;

/**
 * Lance la version non threadee de l'apprentissage en fonction des differents parametres avec la methode k-fold cross-validation
 */
public class ThreadlessLearnAndTest implements Callable<ConfusionMatrix>{
	
	private int k, nbCoucheCachee, nbSteps, lenMatrix;
	private double learningSpeed;
	String path;

	/**
	 * Lance l'apprentissage de tous les PDF
	 * @param path Chemin des pdf a apprendre
	 * @param k nombre de partition pour le k-fold
	 * @param nbCoucheCachee nombre de neurones dans la couche cachee
	 * @param nbSteps nombre d'etapes maximales par etapes
	 * @param lenMatrix taille neurones par type de features
	 * @param learningSpeed vitesse d'apprentissage
	 */
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
		LearningView.setIndeterminate(false);
		pmc.learnAndTest();
		System.out.println("learning ended in : "+(System.nanoTime()-t1)/1000000000+"s");
		
		return pmc.getConfusionMatrix();
	}

}
