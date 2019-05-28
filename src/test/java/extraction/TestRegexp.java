package extraction;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import main.PDF;

public class TestRegexp {

	/*@Test
	public void testPDF() {
		PDF testPDF = new PDF("");
		Regexp.init();
		List<Feature> featuresList = Regexp.getAllFeatures(testPDF.getText());
		
		assertTrue(featuresList.size() == 10);
	}*/
	
	@Test
	public void testMontant() {
		String s = "bonjour, voici de l'argent 15,20 € et 1582,98EUR";
		List<Feature> featuresList = Regexp.getAllFeatures(s);
		
		assertTrue(featuresList.contains(new Feature(27, "15,20", 0)));
		assertTrue(featuresList.contains(new Feature(38, "1582,98", 0)));
	}
	
	@Test
	public void testDate() {
		String s = "Bonjour, êtes vous disponible à la date 12/18/2017. puis 02.03.2009 20191320";
		List<Feature> featuresList = Regexp.getAllFeatures(s);
		
		assertTrue(featuresList.contains(new Feature(40, "12/18/2017", 1)));
		assertTrue(featuresList.contains(new Feature(57, "02.03.2009", 1)));
		assertTrue(featuresList.contains(new Feature(68, "20191320", 1)));
	}
	
	@Test
	public void testAddresse() {
		String s = "15 rue du poulet frit \n25b AVENUE ALATA \n ceci n'est pas une addresse \n15 euros HT\n120 bis impasse du lol";
		List<Feature> featuresList = Regexp.getAllFeatures(s);
		
		assertTrue(featuresList.contains(new Feature(0, "15 rue du poulet frit", 2)));
		assertTrue(featuresList.contains(new Feature(23, "25b AVENUE ALATA", 2)));
		assertTrue(featuresList.contains(new Feature(83, "120 bis impasse du lol", 2)));
	}
	
	@Test
	public void testCode() {
		String s = "R2D2 FR201536F7 dFerRG10235 fdfd7dff 75456d56684 code58 ADSJFJK";
		List<Feature> featuresList = Regexp.getAllFeatures(s);
		
		assertTrue(featuresList.contains(new Feature(5, "FR201536F7", 3)));
		assertTrue(featuresList.contains(new Feature(16, "dFerRG10235", 3)));
		assertTrue(featuresList.contains(new Feature(28, "fdfd7dff", 3)));
		assertTrue(featuresList.contains(new Feature(37, "75456d56684", 3)));
		assertTrue(featuresList.contains(new Feature(49, "code58", 3)));
	}

}
