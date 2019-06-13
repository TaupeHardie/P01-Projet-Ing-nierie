package apprentissage;

import java.io.File;

/**
 * Structure qui garde le score associé au pattern
 * 
 */
public class Sortie implements Comparable<Sortie>{
	public String patternName;
	public File pdf;
	public double score;
	
	/**
	 * Constructeur par defaut
	 * @param file
	 * @param patternName
	 * @param score
	 */
	public Sortie(File pdf, String patternName, double score) {
		super();
		this.patternName = patternName;
		this.score = score;
		this.pdf = pdf;
	}

	@Override
	/**
	 * compare le score au score de la sortie
	 * @return 1 ou -1
	 */
	public int compareTo(Sortie o) {
		if(score == o.score)
			return 0;
		else if( score < o.score)
			return -1;
		else
			return 1;
	}

	/**
	 * Renvoie la sortie correspondant à la somme des scores
	 * @param sortie 
	 * @return
	 */
	public Sortie add(Sortie sortie) {
		Sortie newSortie = new Sortie(pdf, patternName, score + sortie.score);
		return newSortie;
	}

	/**
	 * Renvoie la sortie correspondant à la division des scores
	 * @param d
	 * @return
	 */
	public Sortie norm(double d) {
		Sortie newSortie = new Sortie(pdf, patternName, score / d);
		return newSortie;
	}
	
}
