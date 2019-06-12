package apprentissage;

/**
 * Structure qui contient le chemin d'un pdf et de sa classe associee
 *
 */
public class Sample {
	public String name, category;
	public int number;

	/**
	 * Constructeur par defaut
	 */
	public Sample() {
		name = "";
		category = "";
		number=-1;
	}
	/**
	 * Constructeur
	 * @param name
	 * @param category
	 */
	public Sample(String name, String category) {
		this.name = name;
		this.category = category;
		this.number=-1;
	}

	/**
	 * Constructeur
	 * @param name
	 * @param category
	 * @param number
	 */
	public Sample(String name, String category, int number) {
		this.name = name;
		this.category = category;
		this.number=number;
	}
	
	@Override
	/**
	 * transforme les champs en string
	 * @return string 
	 */
	public String toString() {
		return "Sample [name=" + name + ", category=" + category + ", number="+ number +"]";
	}
}
