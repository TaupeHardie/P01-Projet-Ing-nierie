package extraction;

/**
 * Structure qui contient l'information des éléments récupérés
 *
 */

public class Feature {
	private int pos;
	private String str;
	private String type;
	
	
	/**
	 * Constructeur par défaut
	 */
	public Feature() {
		pos = -1;
		type = "UKN";
		str = "";
	}
	
	/**
	 * Contructeur
	 * @param pos position
	 * @param str chaine correspondante
	 * @param type type du regex
	 */
	public Feature(int pos, String str, String type) {
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
	public void set(int pos, String str, String type) {
		this.pos = pos;
		this.str = str;
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pos;
		result = prime * result + ((str == null) ? 0 : str.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Feature other = (Feature) obj;
		if (pos != other.pos)
			return false;
		if (str == null) {
			if (other.str != null)
				return false;
		} else if (!str.equals(other.str))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public int getPos() {
		return pos;
	}

	public String getType() {
		return type;
	}

	public String getStr() {
		return str;
	}
}
