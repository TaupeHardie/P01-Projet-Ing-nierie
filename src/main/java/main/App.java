package main;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import apprentissage.DataManager;
import apprentissage.PMC;
import misc.Const;
import resources.ResourcesLoader;
import view.LearningView;


public class App {

	static {
	    System.setProperty("org.apache.commons.logging.Log",
	                 "org.apache.commons.logging.impl.NoOpLog");
	}
	
	
	public static void main(String[] args) {
		LearningView.main(args);
	}
}
