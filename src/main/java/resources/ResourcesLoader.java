package resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourcesLoader {
	/**
	 * load a file from resources
	 * @param fileName the name of the resource under src/main/resources/
	 * @return the file if present or an exception
	 */
    public static File loadResourceFile(String fileName) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file : "+fileName+" is not found!");
        } else {
            return new File(resource.getFile());
        }
    }
    
    /**
     * load all the pdf that are in the folder and the subfolders
     * @param directoryPath the folder to be loaded
     * @return a list with all the pdf
     */
    public static List<File> loadDirectory(String directoryPath){
    	List<File> files = new ArrayList<File>();
    	
    	File directory = new File(directoryPath);
    	for (final File fileEntry : directory.listFiles()) {
            if (fileEntry.isDirectory()) {
            	files.addAll(loadDirectory(fileEntry.getPath()));
            } else if(fileEntry.getPath().contains(".pdf")){
            	files.add(fileEntry);
            }
        }
    	
    	return files;
    }
    
    public static List<String> getDirectoriesName(String rootPath){
    	List<String> names = new ArrayList<String>();
    	File directory = new File(rootPath);
    	for (final File fileEntry : directory.listFiles()) {
    		if (fileEntry.isDirectory()) {
    			names.add(fileEntry.getName());
    		}
    	}
    	return names;
    }
    
    public static void readFile(String fileName) {
    	File f = loadResourceFile(fileName);
    	
    	FileInputStream fis = null;
		try {
			 fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InputStreamReader isr = new InputStreamReader(fis);
		BufferedReader buff = new BufferedReader(isr);
		
		String ligne = null;
		try {
			ligne = buff.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (ligne != null) {
			System.out.println(ligne);
			try {
				ligne = buff.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			isr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			buff.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}
