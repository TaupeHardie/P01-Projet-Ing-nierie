package main;

import java.io.File;
import java.util.List;

import extraction.Feature;

import resources.ResourcesLoader;

import extraction.Regexp;

import writer.Writer;

public class App {

	public static final String MainPath = System.getProperty("user.home")+"\\AppData\\Local\\Qweeby\\";
	
	public static void main(String[] args) {



		/* Version initiale
//		PDF testPDF = new PDF("src/main/resources/pdf/CESTIA/FR64489938837_004022_2019.pdf"); 
//		//PDF testPDF = new PDF(ResourcesLoader.loadFile("pdf/Betafonce/8911100750_2019-01_PY4330_SD4330[]_signed.pdf")); 
//		System.out.println(testPDF); 
//		List<Feature> features = testPDF.findMatches(); 
//		 
//		StringBuilder texte = new StringBuilder(); 
//		for (Feature f : features) { 
//			texte.append(f.toString()+"\r\n"); 
//		} 
//		Writer.writeTo(texte.toString(), "output.txt"); 
//		*/
//		
//		// cr�ation d'un texte avec et sans �l�ment cach� � partir d'un pdf
//		PDF testPDF = new PDF(MainPath+"pdf/CESTIA/FR64489938837_004022_2019.pdf");
//		//PDF testPDF = new PDF(ResourcesLoader.loadFile("pdf/Betafonce/8911100750_2019-01_PY4330_SD4330[]_signed.pdf"));
//		System.out.println(testPDF);
//		
//		String text=testPDF.getText();
//		String clearText= Regexp.removeHiddenText(text);
//		Writer.writeTo(testPDF.getText(), MainPath+"testcache.txt");
//		Writer.writeTo(clearText, MainPath+"testclear.txt");

	}
}
