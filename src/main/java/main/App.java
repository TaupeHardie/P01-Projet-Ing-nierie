package main;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import apprentissage.DataManager;
import apprentissage.PMC;
import misc.Const;
import resources.ResourcesLoader;


public class App {

	static {
	    System.setProperty("org.apache.commons.logging.Log",
	                 "org.apache.commons.logging.impl.NoOpLog");
	}
	
	
	public static void main(String[] args) {
		
		DataManager dm = new DataManager();
		
		dm.kfoldCrossValidation(10, Const.MainPath+"pdf");

		PMC pmc = new PMC(Const.MainPath+"pdf", 10, 50, 200, 10, 0.001);
		
		long t = System.nanoTime();
		pmc.learnAndTestThread();
		System.out.println("execution time : "+(System.nanoTime() - t)/1000000000);
	}
}
