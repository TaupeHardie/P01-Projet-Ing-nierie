package main;

import java.io.File;
import java.util.List;

import extraction.Feature;
import resources.ResourcesLoader;
import writer.Writer;

public class App {

	public static final String MainPath = System.getProperty("user.home")+"\\AppData\\Local\\Qweeby\\";
	
	public static void main(String[] args) {
		System.out.println(ResourcesLoader.loadDirectory(MainPath+"pdf\\L2G\\"));
	}
}
