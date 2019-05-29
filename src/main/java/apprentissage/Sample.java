package apprentissage;

public class Sample {
	public String name, category;

	public Sample() {
		name = "";
		category = "";
	}
	
	public Sample(String name, String category) {
		this.name = name;
		this.category = category;
	}

	@Override
	public String toString() {
		return "Sample [name=" + name + ", category=" + category + "]";
	}
}
