package main;

import java.io.File;
import java.util.List;

import apprentissage.DataManager;
import apprentissage.PMC;


public class App {

	public static final String MainPath = System.getProperty("user.home")+"\\AppData\\Local\\Qweeby\\";
	
	static {
	    System.setProperty("org.apache.commons.logging.Log",
	                 "org.apache.commons.logging.impl.NoOpLog");
	}
	
	
	public static void main(String[] args) {
		
		DataManager dm = new DataManager();
		dm.kfoldCrossValidation(10, MainPath+"pdf");
		
		PMC pmc = new PMC(10, 50, MainPath+"pdf");
		
		pmc.learnAndTest(MainPath+"pdf");
	}
}
