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
		String s = "bonjour, voici de l'argent 15,20 € et 1582,98EUR. gain de 1000€ et de 15.6USD";
		List<Feature> featuresList = Regexp.getAllFeatures(s);
		
		assertTrue(featuresList.contains(new Feature(27, "15,20", "FeaturePrix")));
		assertTrue(featuresList.contains(new Feature(38, "1582,98", "FeaturePrix")));
		assertTrue(featuresList.contains(new Feature(70, "15.6", "FeaturePrix")));
		assertTrue(featuresList.contains(new Feature(58, "1000", "FeaturePrix")));
	}
	
	@Test
	public void testDate() {
		String s = "10/12/2017, 02.03.2009, 01 08 1990, 12/10/19, 02.03.19, 02 05 19";
		List<Feature> featuresList = Regexp.getAllFeatures(s);
		
		assertTrue(featuresList.contains(new Feature(0, "10/12/2017", "FeatureDate")));
		assertTrue(featuresList.contains(new Feature(12, "02.03.2009", "FeatureDate")));
		assertTrue(featuresList.contains(new Feature(24, "01 08 1990", "FeatureDate")));
		assertTrue(featuresList.contains(new Feature(36, "12/10/19", "FeatureDate")));
		assertTrue(featuresList.contains(new Feature(46, "02.03.19", "FeatureDate")));
		assertTrue(featuresList.contains(new Feature(56, "02 05 19", "FeatureDate")));
		
		String s2 = "2019 02 05";
		List<Feature> featuresList2 = Regexp.getAllFeatures(s2);
		assertTrue(featuresList2.contains(new Feature(0, "2019 02 05", "FeatureDate")));
		
	}
	
	@Test
	public void testAddresse() {
		String s = "15 rue du poulet frit \n25, AVENUE ALATA \n ceci n'est pas une addresse \n15 euros HT\n120 bis impasse du lol";
		List<Feature> featuresList = Regexp.getAllFeatures(s);
		assertTrue(featuresList.contains(new Feature(0, "15 rue du poulet frit", "FeatureAddresse")));
		assertTrue(featuresList.contains(new Feature(23, "25b AVENUE ALATA", "FeatureAddresse")));
		assertTrue(featuresList.contains(new Feature(83, "120 bis impasse du lol", "FeatureAddresse")));

	}
	
	@Test
	public void testCode() {
		String s = "R2D2 FR201536F7 dFerRG10235 fdfd7dff 75456d56684 code58 ADSJFJK";
		List<Feature> featuresList = Regexp.getAllFeatures(s);
		
		assertTrue(featuresList.contains(new Feature(5, "FR201536F7", "FeatureCode")));
		assertTrue(featuresList.contains(new Feature(16, "dFerRG10235", "FeatureCode")));
		assertTrue(featuresList.contains(new Feature(28, "fdfd7dff", "FeatureCode")));
		assertTrue(featuresList.contains(new Feature(37, "75456d56684", "FeatureCode")));
		assertTrue(featuresList.contains(new Feature(49, "code58", "FeatureCode")));
	}

}
