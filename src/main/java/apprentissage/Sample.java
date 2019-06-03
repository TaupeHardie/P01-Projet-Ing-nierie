package apprentissage;

/**
 * Structure qui contient le chemin d'un pdf et de sa classe associee
 *
 */
public class Sample {
	public String name, category;
	public int number;

	public Sample() {
		name = "";
		category = "";
		number=-1;
	}
	
	public Sample(String name, String category) {
		this.name = name;
		this.category = category;
		this.number=-1;
	}

	public Sample(String name, String category, int number) {
		this.name = name;
		this.category = category;
		this.number=number;
	}
	
	@Override
	public String toString() {
		return "Sample [name=" + name + ", category=" + category + ", number="+ number +"]";
	}
}
