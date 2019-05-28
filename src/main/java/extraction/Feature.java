package extraction;

public class Feature {
	int pos;
	String str;
	
	
	/**
	 * Constructeur par défaut
	 */
	public Feature() {
		pos = -1;
		str = "";
	}
	
	/**
	 * Contructeur
	 * @param pos position
	 * @param str chaine correspondante
	 */
	public Feature(int pos, String str) {
		this.pos = pos;
		this.str = str;
	}
	
	/**
	 * Affiche les informations
	 */
	public void print() {
		System.out.println(pos + " " + str);
	}
	
	/**
	 * Setter
	 * @param pos position
	 * @param str chaine de caractères
	 */
	public void set(int pos, String str) {
		this.pos = pos;
		this.str = str;
	}
}
