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

		PMC pmc = new PMC(10, 50, Const.MainPath+"pdf");
		
		pmc.learnAndTestThread();
	}
}
