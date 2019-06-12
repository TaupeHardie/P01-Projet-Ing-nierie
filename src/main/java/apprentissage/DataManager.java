package apprentissage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import resources.ResourcesLoader;

/**
 * Contient tous les fichiers a traiter pour l'apprentissage et pour les tests
 */
public class DataManager {
	private List<List<Sample>> data;
	private int k, nbClasses;
	private String path;
	
	/**
	 * Constructeur par defaut
	 */
	public DataManager(String path) {
		data = new ArrayList<List<Sample>>();
		this.path = path;
		ResourcesLoader.loadResourcesIn(path);
	}
	
	public List<List<Sample>> getData() {
		return this.data;
	}
	
	public int getK() {
		return k;
	}
	
	public int numClasses() {
		return nbClasses;
	}
	
	/**
	 * Effectue une k-fold cross-validation. Decoupe les donnees en k parties.
	 * Une partie sera utilisee pour la partie test, le reste pour l'apprentissage
	 * @param k
	 */
	public void kfoldCrossValidation(int k) {
		List<String> directoryName = ResourcesLoader.getDirectoriesName();
		directoryName.remove("_IGNORE");
		
		this.k = k;
		this.nbClasses = directoryName.size();
		
		data.clear();
		
		for(int i = 0; i < k; i++) {
			data.add(new ArrayList<Sample>());
			
			for(String dn:directoryName) {
				List<File> pdf = ResourcesLoader.loadFileIn(path+"/"+ dn);
				int step = pdf.size()/k;
				int limit = i == k-1 ? pdf.size():(i+1)*step;

				for(int j = i*step; j < limit; j++) {
					data.get(i).add(new Sample(pdf.get(j).getPath(), dn, directoryName.indexOf(dn)));
				}
			}
		}
	}
	
	/**
	 * Retourne le nombre de donnees recuperees
	 * @return nombre de donnees recuperees
	 */
	public int getSampleNumber() {
		int n = 0;
		for(int i = 0; i < data.size(); i++) {
			n += data.get(i).size();
		}
		
		return n;
	}
}
