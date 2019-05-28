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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pos;
		result = prime * result + ((str == null) ? 0 : str.hashCode());
		result = prime * result + type;
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
		if (type != other.type)
			return false;
		return true;
	}

	public int getPos() {
		return pos;
	}

	public int getType() {
		return type;
	}

	public String getStr() {
		return str;
	}
}
