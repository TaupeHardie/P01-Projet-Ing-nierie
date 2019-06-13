package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.sun.org.apache.xml.internal.serialize.TextSerializer;

import Controller.ThreadCompute;
import apprentissage.PMC;
import apprentissage.Sortie;
import misc.Const;
import misc.PDF;
import resources.ResourcesLoader;

/**
 * classe qui affiche l'onglet principal de la vue client, permet de selectionner des fichiers et de voir la progression.
 * Cette classe lance la logique dans un thread separe.
 */
public class MainPane extends JPanel {
	private JTextField txtSelectionezUnDossier;
	private JFileChooser fileChooser;
	private static JLabel lblTraitement;
	private static JProgressBar progressBar;
	private static Vector<Future<Vector<Sortie>>> sortieListe;
	
	private final String lblEnCours = "Veuillez patienter, traitement en cours ...";
	private final String lblSelectDir = "selectionnez un dossier ...";
	private final String lblBtnTraiter = "Traiter les documments";
	private final String lblFileChooser = "selectionnez un dossier";
	
	/**
	 * Create the panel.
	 */
	public MainPane() {
		sortieListe = new Vector<Future<Vector<Sortie>>>();
		
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
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setCurrentDirectory(new java.io.File(Const.DesktopPath));
				fileChooser.setDialogTitle(lblFileChooser);
				fileChooser.setAcceptAllFileFilterUsed(false);
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
					Const.WorkingDir = fileChooser.getSelectedFile().toString();
					txtSelectionezUnDossier.setText(fileChooser.getSelectedFile().toString()+"/pdf");
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
				
				//launch the logic in a new thread here
				ResourcesLoader.loadFileIn(txtSelectionezUnDossier.getText());
				PMC pmc = new PMC(txtSelectionezUnDossier.getText());
				String texte = txtSelectionezUnDossier.getText();
				
				progressBar.setMinimum(0);
				progressBar.setValue(0);
				progressBar.setMaximum(ResourcesLoader.getPDFs().size());
				
				ExecutorService executor = Executors.newFixedThreadPool(ResourcesLoader.getPDFs().size());
				
				sortieListe.clear();
				for(PDF pdf:ResourcesLoader.getPDFs() ) {
					sortieListe.add(executor.submit(new ThreadCompute(texte, pdf)));
				}
				
				executor.shutdown();
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
	 * incremente la progress bar d'une unite
	 * Si 100% est atteint met le texte du label à "Termine".
	 */
	public static void incrementProgressBar() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(progressBar.getValue()+1 < progressBar.getMaximum()) {
					progressBar.setValue(progressBar.getValue()+1);
				}else {
					progressBar.setValue(progressBar.getValue()+1);
					lblTraitement.setText("Termine");
					HashMap<String, Vector<Sortie>> sortieParDossier = new HashMap<String, Vector<Sortie>>();
					HashMap<String, Integer> count = new HashMap<String, Integer>();
					
					for(Future<Vector<Sortie>> s:sortieListe) {
						try {
							Vector<Sortie> vecteur = s.get();
							File f = vecteur.get(0).pdf;
							if(sortieParDossier.containsKey(f.getParent())) {
								Vector<Sortie> currVec = sortieParDossier.get(f.getParent());
								if(vecteur.size() != currVec.size())
									System.out.println("prout");
								for(int i = 0; i < currVec.size(); i++) {
									currVec.set(i, currVec.get(i).add(vecteur.get(i)));
								}
								
								count.put(f.getParent(), count.get(f.getParent()) + 1);
							}
							else {
								sortieParDossier.put(f.getParent(), vecteur);
								count.put(f.getParent(), 0);
							}
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
					
					Set<String> keyset = sortieParDossier.keySet();
					
					for(String key:keyset) {
						
						for(int i = 0; i < sortieParDossier.get(key).size(); i++) {
							sortieParDossier.get(key).set(i, sortieParDossier.get(key).get(i).norm((double) count.get(key)));
						}
						
						Collections.sort(sortieParDossier.get(key));
						Collections.reverse(sortieParDossier.get(key));
						ClientView.addPane(sortieParDossier.get(key).get(0).pdf.getParentFile().getName(), sortieParDossier.get(key));
					}
				}
			}
		});
	}
}
