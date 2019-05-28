package main;

import java.util.List;

import extraction.Feature;
import writer.Writer;

public class App {

	public static final String MainPath = System.getProperty("user.home")+"\\AppData\\Local\\Qweeby\\";
	
	public static void main(String[] args) {
		PDF testPDF = new PDF(MainPath+"pdf/Betafonce/8911100750_2019-01_PY4330_SD4330[]_signed.pdf");
		//PDF testPDF = new PDF(ResourcesLoader.loadFile("pdf/Betafonce/8911100750_2019-01_PY4330_SD4330[]_signed.pdf"));
		System.out.println(testPDF);
		List<Feature> features = testPDF.findMatches();
		
		StringBuilder texte = new StringBuilder();
		for (Feature f : features) {
			texte.append(f.toString()+"\r\n");
		}
		Writer.writeTo(texte.toString(), MainPath+"output.txt");
	}
}
