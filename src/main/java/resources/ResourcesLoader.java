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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import misc.Const;
import misc.PDF;
import misc.ShutdownThreads;

/**
 * Classe statique regrouppant toutes les fonctions concernant la lecture de fichiers et de dossiers
 *
 */
public class ResourcesLoader {
	private static List<PDF> pdfs = new ArrayList<PDF>();
	
	
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
    
    /**
     * Get the name of all directories inside the given one
     * @param rootPath the directory to look at
     * @return the name of all directories
     */
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
    
    /**
     * this load all PDF in the list.
     * It launch cores -1 threads to process the PDF and add it to the list in this class
     * @param files the list of pdf files 
     */
    public static void loadAllPdf(List<File> files) {
    	System.out.println("starting PDF loading");
    	ExecutorService service = Executors.newFixedThreadPool(Const.nbCore);
    	int chunk = files.size()/Const.nbCore;
    	for (int i = 0; i < Const.nbCore; i++) {
    		List<File> subFiles = new ArrayList<File>();
    		subFiles.addAll(files.subList(chunk*i, chunk*(i+1)));
			service.execute(new ThreadPDFLoader(subFiles));
		}
    	ShutdownThreads.shutdownAndAwaitTermination(service, 5*60);
    }
    
    /**
     * this method is used by threads to populate the PDF list
     * @param p the PDF
     */
    public static synchronized void addPdf(PDF p) {
    	pdfs.add(p);
    }
    
    /**
     * get the PDF from the PDF list by its name
     * @param name the name of the PDF
     * @return the PDF
     */
    public static PDF getPDFbyName(String name) {
    	List<PDF> pdflst = pdfs.stream().filter(p->p.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
    	if(!pdflst.isEmpty()) {
    		return pdflst.get(0);
    	}
    	return null;
    			
    }

    /**
     * read a file and output the text in the console
     * @param fileName the file to be read 
     */
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
