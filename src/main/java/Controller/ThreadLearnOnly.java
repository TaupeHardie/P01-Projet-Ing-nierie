package Controller;

import apprentissage.PMC;

/**
 * Thread lancant la logique de l'application pour améliorer l'algorithme.
 */
public class ThreadLearnOnly implements Runnable{
	private int k, nbCoucheCachee, nbSteps, lenMatrix;
	private double learningSpeed;
	String path;

	/**
	 * Lance l'apprentissage de tous les PDF
	 * @param path Chemin des pdf à apprendre
	 * @param k nombre de partition pour le k-fold
	 * @param nbCoucheCachee nombre de neurones dans la couche cachée
	 * @param nbSteps nombre d'étapes maximales par étapes
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
		PMC pmc = new PMC(path, k, nbCoucheCachee, nbSteps, lenMatrix, learningSpeed);
		pmc.learnOnly();
	}
}
