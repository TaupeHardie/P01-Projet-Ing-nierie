package main;

import extraction.Regexp;

public class App {

	public static void main(String[] args) {
		Regexp.init();
		PDF testPDF = new PDF("src/main/resources/pdf/Betafonce/8911100750_2019-01_PY4330_SD4330[]_signed.pdf");
		//PDF testPDF = new PDF(ResourcesLoader.loadFile("pdf/Betafonce/8911100750_2019-01_PY4330_SD4330[]_signed.pdf"));
		testPDF.print();
		testPDF.findMatches();
	}
}
