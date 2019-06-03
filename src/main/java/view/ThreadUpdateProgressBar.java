package view;

import javax.swing.JProgressBar;

/**
 * this class is used to update the progressBar in the mainWindow
 * call it to increment the progression by one unit
 * @author axel
 *
 */
public class ThreadUpdateProgressBar implements Runnable {
	private JProgressBar progressBar;
	
	
	public ThreadUpdateProgressBar(JProgressBar progressBar) {
		super();
		this.progressBar = progressBar;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(progressBar.getValue() < progressBar.getMaximum()) {
			progressBar.setValue(progressBar.getValue()+1);
			progressBar.getParent().update(progressBar.getParent().getGraphics());
		}
	}
	
}
