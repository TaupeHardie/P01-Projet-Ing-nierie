package view;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * this class is used to update the progressBar in the MainPane
 * call it to increment the progression by one unit
 * @author axel
 *
 */
public class ThreadUpdateProgressBar implements Runnable {
	private JProgressBar progressBar;
	private JLabel label;
	
	
	public ThreadUpdateProgressBar(JProgressBar progressBar, JLabel label) {
		super();
		this.progressBar = progressBar;
		this.label = label;
	}

	@Override
	public void run() {
		if(progressBar.getValue()+1 < progressBar.getMaximum()) {
			progressBar.setValue(progressBar.getValue()+1);
		}else {
			progressBar.setValue(progressBar.getValue()+1);
			label.setText("Terminé");
		}
		progressBar.update(progressBar.getGraphics());
	}
	
}
