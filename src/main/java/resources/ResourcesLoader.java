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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import misc.Const;
import misc.PDF;
import misc.ShutdownThreads;
import view.ClientView;
import view.ThreadUpdateProgressBar;


public class ResourcesLoader {
	private static List<PDF> pdfs = new ArrayList<PDF>();
	private static String directoryOrFileLoaded = "";
	
	
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
    private static List<File> loadDirectory(String directoryPath){
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
     * load the input witch can be a folder or a file
     * @param path selected by the user it represent a directory or a file
     * @return all the files that are in the directory and it's sub directories or the selected file
     */
    public static List<File> loadFileIn(String path){
    	List<File> files = new ArrayList<File>();
    	File f = new File(path);
    	if(!f.isDirectory() && f.getPath().contains(".pdf")) {
    		files.add(f);
    	}else {
    		files = loadDirectory(path);
    	}
    	return files;
    }
    
    /**
     * this load all PDF in the list.
     * It launch cores -1 threads to process the PDF and add it to the list in this class
     * @param files the list of pdf files 
     */
    private static void loadAllPdf(List<File> files) {
    	ExecutorService service = Executors.newFixedThreadPool(Const.nbCore);
    	int max=0;
    	int chunk=0;
    	if(files.size()<Const.nbCore) {
    		max = files.size();
    		chunk = 1;
    	}else {
    		max = Const.nbCore;
    		chunk = files.size()/Const.nbCore;
    	}
    	for (int i = 0; i < max-1; i++) {
    		List<File> subFiles = new ArrayList<File>();
    		subFiles.addAll(files.subList(chunk*i, chunk*(i+1)));
			service.execute(new ThreadPDFLoader(subFiles));
		}
    	List<File> subFiles = new ArrayList<File>();
		subFiles.addAll(files.subList(chunk*(max-1), files.size()));
		service.execute(new ThreadPDFLoader(subFiles));
		ShutdownThreads.shutdownAndAwaitTermination(service, 5*60);
    }
    
    /**
     * this method is used by threads to populate the PDF list
     * @param p the PDF
     */
    public static synchronized void addPdf(PDF p) {
    	pdfs.add(p);
    }
    
    public static void loadResourcesIn(String path) {
    	if(!path.equalsIgnoreCase(directoryOrFileLoaded)) {
        	System.out.println("start PDF loading");
        	long t1 = System.nanoTime();
        	pdfs.clear();
        	directoryOrFileLoaded = path;
        	loadAllPdf(loadFileIn(directoryOrFileLoaded));
        	System.out.println("PDF loading ended in : "+(System.nanoTime()-t1)/1000000000+"s");
    	}
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
     * Get the name of all directories inside the given one
     * @param rootPath the directory to look at
     * @return the name of all directories
     */
    public static List<String> getDirectoriesName(){
    	List<String> names = new ArrayList<String>();
    	File directory = new File(directoryOrFileLoaded);
    	for (final File fileEntry : directory.listFiles()) {
    		if (fileEntry.isDirectory()) {
    			names.add(fileEntry.getName());
    		}
    	}
    	return names;
    }
    
    /**
     * get All PDF loaded in the class
     * @return the list of all PDF loaded in the class
     */
    public static List<PDF> getPDFs(){
    	return pdfs;
    }
    
    /**
     * read a file and return the text
     * @param fileName the file to be read
     * @return a list that contain every line read
     */
    public static List<String> readFile(String fileName) {
    	List<String> output = new ArrayList<String>();
    	
    	File f = new File(fileName);
    	
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
			output.add(ligne);
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
		return output;
	}
}
