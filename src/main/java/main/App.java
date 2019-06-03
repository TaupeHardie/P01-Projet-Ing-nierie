package main;

import java.io.File;
import java.util.List;

import apprentissage.DataManager;
import apprentissage.PMC;


public class App {

	public static final String MainPath = System.getProperty("user.home")+"\\AppData\\Local\\Qweeby\\";
	
	public static void main(String[] args) {
		DataManager dm = new DataManager();
		dm.kfoldCrossValidation(10);
		apprentissage.PMC.getExpectedResultsMatrix(dm.getData().get(0).get(10));
	}
}
