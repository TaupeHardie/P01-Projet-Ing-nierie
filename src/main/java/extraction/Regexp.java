package extraction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe statique qui regroupe toutes les fonctions concernant les regex
 *
 */
public class Regexp {
	//liste des regexp
	private static List<String> regexps = new ArrayList<String>();
	//List des nom des regexp
	private static List<String> regexpsName = new ArrayList<String>();
	
	public static final String FeaturePrix = "([1-9][0-9]+(,[0-9]{2})?[ ]?[â‚¬]?(EUR)?)";
	public static final String FeatureDate = "([0-9]{2}[./ ]?(?:1[0-2]|0?[1-9])[./ ]?(?:[12][0-9]{1,3}|[0-9][1-9]{1,3}))|((?:[12][0-9]{1,3}|[0-9][1-9]{1,3})[./ ]?(?:1[0-2]|0?[1-9])[./ ]?[0-9]{2})";
	public static final String FeatureAddresse = "(?i)[0-9]+(b|t|d){0,1}(,){0,1}( bis| ter){0,1} (esplanade|impasse|rue|avenue|boulevard) [a-z ]+";
	public static final String FeatureCode = "(?i)(?=(?:\\w*\\d){1,}\\w*)[\\w\\d]{5,}";
	
	/**
	 * init permet de remplir la liste des regexp automatiquement
	 * les regexp sont des atributs de la class (utile pour les Junit test)
	 * Cela utilise la reflextion : liste les champs de la class, on prend les valeurs de tous sauf la liste
	 */
	private static void init() {	
		if(regexps.isEmpty()) {
			Field[] fields = Regexp.class.getDeclaredFields();
			for(Field f : fields) {
				if(f.getName().contains("Feature")) {
					try {
						regexps.add((String) f.get(new Object()));
						regexpsName.add((String) f.getName());
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * detecte toutes les features, matchees par nos regexp, dans le texte en parametre
	 * @param content le texte a traiter
	 * @return une liste des feature detectees par toutes nos regexp
	 */
	public static List<Feature> getAllFeatures(String content) {
		init();
		
		List<Feature> features = new ArrayList<Feature>();
		
		for(int i = 0; i < regexps.size(); i++) {
			Pattern pattern = Pattern.compile(regexps.get(i));
			Matcher matcher = pattern.matcher(content);
			while(matcher.find()) {
				Feature f = new Feature();
				f.set(matcher.start(), matcher.group().trim(), regexpsName.get(i));
				features.add(f);
			}
		}
		return features;
	}
	/** 
	 * supprime tous les textes entre accolades
	 * @param textPDF le texte du PDF avec des elements caches
	 * @return le même document sans element cache
	 */
	public static String removeHiddenText(String textPDF) {
		
		String clearText = textPDF.replaceAll("\\{.*\\}", "").trim();
		return clearText;
	}
}
