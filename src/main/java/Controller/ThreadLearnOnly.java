package Controller;

import apprentissage.PMC;
import resources.ResourcesLoader;
import view.LearningView;

/**
 * Thread lancant la logique de l'application pour ameliorer l'algorithme.
 */
public class ThreadLearnOnly implements Runnable{
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
	public ThreadLearnOnly(String path, int k, int nbCoucheCachee, int nbSteps, int lenMatrix, double learningSpeed) {
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
	public void run() {	
		ResourcesLoader.turnPdfIntoFeatureFile(path);
		PMC pmc = new PMC(path, k, nbCoucheCachee, nbSteps, lenMatrix, learningSpeed);
		LearningView.setIndeterminate(false);
		pmc.learnOnly();
	}
}
