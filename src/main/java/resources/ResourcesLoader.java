package resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ResourcesLoader {
	/**
	 * load a file from resources
	 * @param fileName the name of the resource starting at src/main/resources/fileName
	 * @return the file if present or an exception
	 */
    public static File loadFile(String fileName) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file : "+fileName+" is not found!");
        } else {
            return new File(resource.getFile());
        }
    }
    
    public static void readFile(String fileName) {
    	File f = loadFile(fileName);
    	
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
