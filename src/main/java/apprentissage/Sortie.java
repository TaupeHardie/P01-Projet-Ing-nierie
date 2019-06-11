package apprentissage;

/**
 * Structure qui garde le score associé au pattern
 * 
 */
public class Sortie implements Comparable<Sortie>{
	public String patternName;
	public double score;
	
	public Sortie(String patternName, double score) {
		super();
		this.patternName = patternName;
		this.score = score;
	}

	@Override
	public int compareTo(Sortie o) {
		if(score == o.score)
			return 0;
		else if( score < o.score)
			return -1;
		else
			return 1;
	}
	
}
