package Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

import apprentissage.PMC;
import apprentissage.Sortie;
import misc.PDF;
import resources.ResourcesLoader;
import view.ClientView;
import view.MainPane;
import view.ResultPane;

/**
 * Thread qui lance l'estimation des pdf selectionnes
 */
public class ThreadCompute implements Runnable {
	
	private PDF pdf;
	private String texte;
	
	public ThreadCompute(String texte, PDF pdf) {
		this.pdf = pdf;
		this.texte = texte;
	}

	@Override
	public void run() {		
		PMC pmc = new PMC(texte);
		pmc.compute(pdf);
		Vector<Sortie> s = pmc.getSortie();
		File f = new File(pdf.getName());
		ClientView.addPane(f.getName(), s);
		MainPane.incrementProgressBar();
		
	}

}
