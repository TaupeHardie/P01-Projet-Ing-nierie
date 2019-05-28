package extraction;

public class Feature {
	int pos, type;
	String str;
	
	
	/**
	 * Constructeur par défaut
	 */
	public Feature() {
		pos = -1;
		type = -1;
		str = "";
	}
	
	/**
	 * Contructeur
	 * @param pos position
	 * @param str chaine correspondante
	 * @param type type du regex
	 */
	public Feature(int pos, String str, int type) {
		this.pos = pos;
		this.str = str;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Feature [pos=" + pos + ", type=" + type + ", str=" + str + "]";
	}

	/**
	 * Setter
	 * @param pos position
	 * @param str chaine de caractères
	 * @param type type du regex
	 */
	public void set(int pos, String str, int type) {
		this.pos = pos;
		this.str = str;
		this.type = type;
	}
}
