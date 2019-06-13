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
public class ThreadCompute implements Callable<Vector<Sortie>> {
	
	private PDF pdf;
	private String texte;
	private List<String> dirName;
	
	public ThreadCompute(String texte, PDF pdf, List<String> dirName) {
		this.pdf = pdf;
		this.texte = texte;
		this.dirName = dirName;
	}

	@Override
	public Vector<Sortie> call() throws Exception {
		PMC pmc = new PMC(texte, dirName);
		pmc.compute(pdf);
		Vector<Sortie> s = pmc.getSortie();
		MainPane.incrementProgressBar();
		return s;
	}

}
