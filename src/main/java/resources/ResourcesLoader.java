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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import misc.Const;
import misc.PDF;
import misc.ShutdownThreads;
import view.ClientView;
import writer.ThreadPdfWriter;
import writer.Writer;

/**
 * Class qui charge les resources, le plus souvent des pdf
 */
public class ResourcesLoader {
	private static List<PDF> pdfs = new ArrayList<PDF>();
	private static String directoryOrFileLoaded = "";
	private static BlockingQueue<PDF> queue = new ArrayBlockingQueue<PDF>(300);
	private static int nbpoison =0;
	
	
	/**
	 * Charge un fichier
	 * @param fileName le nom du fichier
	 * @return le fichier si present
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
     * Charge tout les pdfs dans le dossier et dans les sous dossiers
     * @param directoryPath le dossier ou charger
     * @returnla liste des pdf
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
     * Charge le(s) fichier(s) selectionne(s) ou ceux dans le dossier selectionne.
     * @param path un fichier ou un dossier
     * @return tous les fichiers dans le dossier ou selectionne. 
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
     * Charge tous les pdfs dans la classe.
     * lance des threads pour ce faire.
     * @param files la liste des fichiers a charger. 
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
     * Lit les pdf et les ajoute dans la file de traitement.
     * lance des threads pour ce faire.
     * @param files la liste des fichiers a charger. 
     */
    private static void ReadAllPdf(List<File> files) {
    	ExecutorService service = Executors.newFixedThreadPool(Const.nbCore+1);
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
			service.execute(new ThreadPDFReader(subFiles, queue));
		}
    	List<File> subFiles = new ArrayList<File>();
		subFiles.addAll(files.subList(chunk*(max-1), files.size()));
		service.execute(new ThreadPDFReader(subFiles, queue));
		
		service.execute(new ThreadPdfWriter(queue, Const.StorePath));
		
		ShutdownThreads.shutdownAndAwaitTermination(service, 5*60);
    }
    
    /**
     * Cette methode permet d'ajouter les PDF dans la liste depuis un thread
     * @param p le PDF
     */
    public static synchronized void addPdf(PDF p) {
    	pdfs.add(p);
    }
    
    /**
     * Charge tous les pdf dans le dossier et ses sous dossier
     * @param path le chemin du dossier
     */
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
     * Recupere le PDF avec son nom.
     * @param name le nom du PDF
     * @return le PDF
     */
    public static PDF getPDFbyName(String name) {
    	List<PDF> pdflst = pdfs.stream().filter(p->p.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
    	if(!pdflst.isEmpty()) {
    		return pdflst.get(0);
    	}
    	return null;		
    }
    
    /**
     * Recupere le nom de tous les sous dossiers (de niveau 1) du dossier selectione. 
     * @param rootPath le dossier a chercher
     * @return le nom des sous dossiers
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
     * Retourne tous les PDF charge dans la classe
     * @return tous les PDF charge dans la classe
     */
    public static List<PDF> getPDFs(){
    	return pdfs;
    }
    
    /**
     * transforme les pfds en liste de features dans un fichier texte
     * @param path le fichier ou ecrire
     */
    public static void turnPdfIntoFeatureFile(String path) {
    	if(!path.equalsIgnoreCase(directoryOrFileLoaded)) {
    		System.out.println("start PDF loading");
        	long t1 = System.nanoTime();
        	
        	directoryOrFileLoaded = path;
        	Writer.clearFile(Const.StorePath);
        	ReadAllPdf(loadFileIn(directoryOrFileLoaded));
        	
        	System.out.println("PDF loading ended in : "+(System.nanoTime()-t1)/1000000000+"s");
    	}
    }
    
    /**
     * Quand les n thread ont fini leur travail met un element indiquant au thread d'ecriture de s'arreter
     */
    public static synchronized void poisonQueue() {
    	nbpoison++;
    	if(nbpoison ==Const.nbCore) {
    		try {
				queue.put(new PDF("END", null));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * Cree si besoin les dossiers et fichiers pour travailler
     */
    public static void createWorkingDirectory() {
    	File f = new File(Const.MainPath);
		if(!f.exists()) {
			try {
				Files.createDirectories(f.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		File f2 = new File(Const.StorePath) ;
		if(!f2.exists()) {
			try {
				Files.createFile(f2.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
