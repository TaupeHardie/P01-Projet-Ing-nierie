package extraction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Regexp {
	//liste des regexp
	private static List<String> regexps = new ArrayList<String>();
	
	private static String lamber = "ETS PAUL LAMBERT";
	private static String paul = "ETS PAUL ONLY";
	
	public static void init() {
		
		regexps.add("ETS PAUL LAMBERT");
	}
	
	public static List<Feature> getAllFeatures(String content) {
		
		
		List<Feature> features = new ArrayList<Feature>();
		
		for(String regexp : regexps) {
			Pattern pattern = Pattern.compile(regexp);
			Matcher matcher = pattern.matcher(content);
			while(matcher.find()) {
				Feature f = new Feature();
				f.set(matcher.start(), matcher.group());
				features.add(f);
			}
		}
		
		return features;
	}
}
