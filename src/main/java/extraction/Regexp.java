package extraction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Regexp {
	//liste des regexp
	private static List<String> regexps = new ArrayList<String>();
	
	public static final String prix = "[0-9]+,[0-9]{2}";
	public static final String date = "\\d{2}/\\d{2}/\\d{4}|\\d{2}\\.\\d{2}\\.\\d{4}";
	public static final String addresse = "(?i)[0-9]+ (rue|avenue|boulevard) [a-z ]+";
	public static final String code = "(?i)([0-9a-z]{0,}[0-9][0-9a-z]{0,}){5,}";
	
	public static void init() {	
		if(regexps.isEmpty()) {
			Field[] fields = Regexp.class.getDeclaredFields();
			for(Field f : fields) {
				if(f.getName() != "regexps") {
					try {
						regexps.add((String) f.get(new Object()));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static List<Feature> getAllFeatures(String content) {
		init();
		
		List<Feature> features = new ArrayList<Feature>();
		
		for(int i = 0; i < regexps.size(); i++) {
			Pattern pattern = Pattern.compile(regexps.get(i));
			Matcher matcher = pattern.matcher(content);
			while(matcher.find()) {
				Feature f = new Feature();
				f.set(matcher.start(), matcher.group(), i);
				features.add(f);
			}
		}
		
		return features;
	}
}
