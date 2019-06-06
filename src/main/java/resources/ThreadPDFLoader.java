package resources;

import java.io.File;
import java.util.List;
import java.util.Vector;

import misc.PDF;

/**
 * this thread is used to populate the list of PDF of ResourcesLoader 
 * It take severals PDF to be process
 * @author axel
 */
public class ThreadPDFLoader implements Runnable {
	 private List<File> files;
 	
	public ThreadPDFLoader(List<File> files) {
		super();
		this.files = files;
	}

	@Override
	public void run() {
		for(File f : files) {
			PDF p = new PDF(f);
			ResourcesLoader.addPdf(p);
		}
	}
}
