package Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import apprentissage.PMC;
import apprentissage.Sortie;
import misc.PDF;
import resources.ResourcesLoader;
import view.ClientView;
import view.MainPane;
import view.ResultPane;

/**
 * Thread qui lance l'estimation des pdf selectionnés
 *
 */
public class ThreadCompute implements Runnable {
	
	private PDF pdf;
	private PMC pmc;
	
	
	public ThreadCompute(PMC pmc, PDF pdf) {
		this.pdf = pdf;
		this.pmc = pmc;
	}

	@Override
	public void run() {		
		pmc.compute(pdf);
		List<Sortie> s = pmc.getSortie();
		File f = new File(pdf.getName());
		ClientView.addPane(f.getName(), s);
		MainPane.incrementProgressBar();
		
	}

}
