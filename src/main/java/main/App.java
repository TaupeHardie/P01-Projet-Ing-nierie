package main;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import apprentissage.DataManager;
import apprentissage.PMC;
import resources.ResourcesLoader;


public class App {

	public static final String MainPath = System.getProperty("user.home")+"\\AppData\\Local\\Qweeby\\";
	
	static {
	    System.setProperty("org.apache.commons.logging.Log",
	                 "org.apache.commons.logging.impl.NoOpLog");
	}
	
	
	public static void main(String[] args) {
		
		DataManager dm = new DataManager();
		
		dm.kfoldCrossValidation(10, MainPath+"pdf");
		
		long t1 = System.nanoTime();
		List<File> files = ResourcesLoader.loadDirectory(MainPath+"pdf");
		ResourcesLoader.loadAllPdf(files);
		System.out.println("kfold time : "+(System.nanoTime() - t1)/1000000000);
		
		
		PMC pmc = new PMC(10, 50, MainPath+"pdf");
		
		pmc.learnAndTest(MainPath+"pdf");
	}
}
