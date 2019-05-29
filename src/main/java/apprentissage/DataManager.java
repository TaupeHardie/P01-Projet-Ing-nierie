package apprentissage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import resources.ResourcesLoader;

/**
 * Contient tous les fichiers à traiter pour l'apprentissage et pour les test
 */
public class DataManager {
	List<List<Sample>> data;
	
	/**
	 * Constructeur par défaut
	 */
	public DataManager() {
		data = new ArrayList<List<Sample>>();
	}
	
	/**
	 * Effectue une k-fold cross-validation. Découpe les données en k parties.
	 * Une partie sera utilisé pour la partie test, le reste pour l'apprentissage
	 * @param k
	 */
	public void kfoldCrossValidation(int k) {
		String root = "src/main/resources/pdf";
		List<String> directoryName = ResourcesLoader.getDirectoriesName(root);
		
		data.clear();
		
		directoryName.remove("_IGNORE");
		
		for(int i = 0; i < k; i++) {
			data.add(new ArrayList<Sample>());
			
			for(String dn:directoryName) {
				List<File> pdf = ResourcesLoader.loadDirectory(root + "/" + dn);
				int step = pdf.size()/k;
				int limit = i == k-1 ? pdf.size():(i+1)*step;

				for(int j = i*step; j < limit; j++) {
					data.get(i).add(new Sample(pdf.get(j).getPath(), dn));
				}
			}
		}
	}
	
	/**
	 * Retourne le nombre de données récupérées
	 * @return nombre de données récupérées
	 */
	public int getSampleNumber() {
		int n = 0;
		for(int i = 0; i < data.size(); i++) {
			n += data.get(i).size();
		}
		
		return n;
	}
}
