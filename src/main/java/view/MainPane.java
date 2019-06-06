package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import resources.ResourcesLoader;

/**
 * class that display the filechooser, a progressBar, and launch the logic in a separate thread
 * @author axel
 */
public class MainPane extends JPanel {
	private JTextField txtSelectionezUnDossier;
	private JFileChooser fileChooser;
	private static JLabel lblTraitement;
	private static JProgressBar progressBar;
	
	private final String lblEnCours = "Veuillez patienter, traitement en cours ...";
	private final String lblSelectDir = "selectionnez un dossier ...";
	private final String lblBtnTraiter = "Traiter les documments";
	private final String lblFileChooser = "selectionnez un dossier";
	
	/**
	 * Create the panel.
	 */
	public MainPane() {
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		
		Box verticalBox = Box.createVerticalBox();
		add(verticalBox, BorderLayout.NORTH);
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentY(0.5f);
		verticalBox.add(horizontalBox);
		
		//file chooser allow to select files and directories, the getSelectedFiles return the path of the directory/file
		txtSelectionezUnDossier = new JTextField();
		txtSelectionezUnDossier.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.setCurrentDirectory(new java.io.File("."));
				fileChooser.setDialogTitle(lblFileChooser);
				fileChooser.setAcceptAllFileFilterUsed(false);
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
					txtSelectionezUnDossier.setText(fileChooser.getSelectedFile().toString());
					txtSelectionezUnDossier.setForeground(Color.black);
				}
			}
		});
	
		txtSelectionezUnDossier.setForeground(Color.LIGHT_GRAY);
		txtSelectionezUnDossier.setText(lblSelectDir);
		horizontalBox.add(txtSelectionezUnDossier);
		txtSelectionezUnDossier.setColumns(10);
		
		
		JButton btnTraiterDoc = new JButton(lblBtnTraiter);
		btnTraiterDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if the text hasn't changed fire an event to open the filechooser dialog
				if(txtSelectionezUnDossier.getText().equalsIgnoreCase(lblSelectDir)) {
					txtSelectionezUnDossier.dispatchEvent(new MouseEvent(txtSelectionezUnDossier, MouseEvent.MOUSE_CLICKED, 0, 0, 100, 100, 1, false));
				}
				lblTraitement.setVisible(true);
				lblTraitement.setText(lblEnCours);
				
				//get all files to be processed
				List<File> lst = ResourcesLoader.loadInput(txtSelectionezUnDossier.getText());
				
				progressBar.setMinimum(0);
				progressBar.setMaximum(lst.size());
				
				//launch the logic in a new thread here
			
			}
		});
		horizontalBox.add(btnTraiterDoc);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut);
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		verticalBox.add(progressBar);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1);
		
		lblTraitement = new JLabel(lblEnCours);
		lblTraitement.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(lblTraitement);
		lblTraitement.setVisible(false);
	}
	
	/**
	 * increment the progress bar by one unit
	 * If it reach maxValue set the label to "Terminé"
	 */
	public static void incrementProgressBar() {
		if(progressBar.getValue()+1 < progressBar.getMaximum()) {
			progressBar.setValue(progressBar.getValue()+1);
		}else {
			progressBar.setValue(progressBar.getValue()+1);
			lblTraitement.setText("Terminé");
		}
		progressBar.getParent().update(progressBar.getParent().getGraphics());
	}
	
	/**
	 * get the progress bar, for thread utilities
	 * @return the progressBar
	 */
	public static JProgressBar getProgressBar() {
		return progressBar;
	}
	
	/**
	 * get the label under the progressBar for thread utilities
	 * @return the label
	 */
	public static JLabel getLabel() {
		return lblTraitement;
	}
}