package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.ejml.simple.SimpleMatrix;

import Controller.ThreadLearnAndTest;
import apprentissage.ConfusionMatrix;
import main.App;
import resources.ResourcesLoader;
import writer.Writer;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JProgressBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.SwingConstants;

/**
 * Gui for QWEEBY, It process pdf to train the algorithm 
 * @author axel
 */
public class LearningView extends JFrame {

	private JPanel contentPane;
	private JTextField txtSelectionezUnDossier;
	private JFileChooser fileChooser;
	private JFileChooser fileSaver;
	private static JLabel lblTraitement;
	private static JProgressBar progressBar;
	private static JButton btnExport;
	private static JLabel lblRappel;
	private static JLabel lblPrecision;
	private static JButton btnTraiterDoc;
	private JRadioButton rdbtnApprentissage;
	private JRadioButton rdbtnK_fold;
	private static Future<ConfusionMatrix> matrix;
	private JSpinner kspinner;
	private JSpinner nbspinner;
	private JSpinner itspinner;
	private JSpinner lenspinner;
	private JTextField textField;
	
	private final String lblEnCours = "Veuillez patienter, traitement en cours ...";
	private final String lblSelectDir = "selectionnez un dossier ...";
	private final String lblBtnTraiter = "Traiter les documments";
	private final String lblMenuFichier = "Fichier";
	private final String lblMenuOption = "Option";
	private final String lblMenuAide = "Aide";
	private final String lblMFichierSauvegarder = "sauvegarder";
	private final String lblMFichierFermer = "fermer";
	private final String lblMAideAPropos = "A Propos";
	private final String lblFileChooser = "selectionnez un dossier";
	
	
	static {
	    System.setProperty("org.apache.commons.logging.Log",
	                 "org.apache.commons.logging.impl.NoOpLog");
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LearningView frame = new LearningView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LearningView() {
		setTitle("QWEEBY - Apprentissage");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 667, 356);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu(lblMenuFichier);
		menuBar.add(mnFichier);
		
		JMenuItem mntmFichierSauvegarder = new JMenuItem(lblMFichierSauvegarder);
		mnFichier.add(mntmFichierSauvegarder);
		
		JMenuItem mntmFichierFermer = new JMenuItem(lblMFichierFermer);
		mnFichier.add(mntmFichierFermer);
		
		JMenu mnOption = new JMenu(lblMenuOption);
		menuBar.add(mnOption);
		
		JMenu mnAide = new JMenu(lblMenuAide);
		menuBar.add(mnAide);
		
		JMenuItem mntmAPropos = new JMenuItem(lblMAideAPropos);
		mnAide.add(mntmAPropos);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		Box verticalBox = Box.createVerticalBox();
		contentPane.add(verticalBox, BorderLayout.NORTH);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);
		
		rdbtnApprentissage = new JRadioButton("Apprentissage complet");
		horizontalBox_1.add(rdbtnApprentissage);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalBox_1.add(horizontalStrut);
		
		rdbtnK_fold = new JRadioButton("K-fold");
		rdbtnK_fold.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				kspinner.setEnabled(rdbtnK_fold.isSelected());
			}
		});
		horizontalBox_1.add(rdbtnK_fold);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnApprentissage);
		group.add(rdbtnK_fold);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		horizontalBox_1.add(horizontalStrut_2);
		
		kspinner = new JSpinner();
		kspinner.setEnabled(false);
		kspinner.setModel(new SpinnerNumberModel(10, 1, 100, 1));
		kspinner.setMaximumSize(new Dimension(60, 30));
		horizontalBox_1.add(kspinner);
		
		Component verticalStrut_5 = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut_5);
		
		Box horizontalBox_3 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_3);
		
		JLabel lblNewLabel = new JLabel("Nombre de neurones dans la couche cachée : ");
		horizontalBox_3.add(lblNewLabel);
		
		nbspinner = new JSpinner();
		nbspinner.setModel(new SpinnerNumberModel(50, 1, 1000, 1));
		nbspinner.setMaximumSize(new Dimension(80, 30));
		
		horizontalBox_3.add(nbspinner);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		horizontalBox_3.add(horizontalStrut_3);
		
		JLabel lblNombreDitrations = new JLabel("Nombre d'itérations : ");
		horizontalBox_3.add(lblNombreDitrations);
		
		itspinner = new JSpinner();
		itspinner.setMaximumSize(new Dimension(60, 30));
		itspinner.setModel(new SpinnerNumberModel(new Integer(200), new Integer(1), null, new Integer(1)));
		horizontalBox_3.add(itspinner);
		
		Component verticalStrut_3 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_3);
		
		Box horizontalBox_4 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_4);
		
		JLabel lblVitesseDapprentissage = new JLabel("Vitesse d'apprentissage");
		horizontalBox_4.add(lblVitesseDapprentissage);
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		horizontalBox_4.add(horizontalStrut_4);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		textField.setText("0.002");
		textField.setMaximumSize(new Dimension(80, 30));
		horizontalBox_4.add(textField);
		textField.setColumns(10);
		
		
		Component horizontalStrut_5 = Box.createHorizontalStrut(20);
		horizontalBox_4.add(horizontalStrut_5);
		
		JLabel lblTailleEntre = new JLabel("Taille de l'entrée : ");
		horizontalBox_4.add(lblTailleEntre);
		
		lenspinner = new JSpinner();
		lenspinner.setMaximumSize(new Dimension(80, 30));
		lenspinner.setModel(new SpinnerNumberModel(10, 1, 10000, 1));
		horizontalBox_4.add(lenspinner);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut);
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setAlignmentY(0.5f);
		verticalBox.add(horizontalBox);
		
				//file chooser allow to select files and directories, the getSelectedFiles return the path of the directory/file
				txtSelectionezUnDossier = new JTextField();
				txtSelectionezUnDossier.setHorizontalAlignment(SwingConstants.LEFT);
				txtSelectionezUnDossier.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						fileChooser.setCurrentDirectory(new java.io.File(App.MainPath));
						fileChooser.setDialogTitle(lblFileChooser);
						fileChooser.setAcceptAllFileFilterUsed(false);
						if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
							txtSelectionezUnDossier.setText(fileChooser.getSelectedFile().toString());
						}
					}
				});
				
				txtSelectionezUnDossier.setForeground(Color.LIGHT_GRAY);
				txtSelectionezUnDossier.setText(lblSelectDir);
				horizontalBox.add(txtSelectionezUnDossier);
				txtSelectionezUnDossier.setColumns(10);
				
				
				btnTraiterDoc = new JButton(lblBtnTraiter);
				btnTraiterDoc.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//If the text hasn't changed fire an event to open the filechooser dialog
						if(txtSelectionezUnDossier.getText().equalsIgnoreCase(lblSelectDir)) {
							txtSelectionezUnDossier.dispatchEvent(new MouseEvent(txtSelectionezUnDossier, MouseEvent.MOUSE_CLICKED, 0, 0, 100, 100, 1, false));
						}
						//If one of the radio button hasn't been check show a dialog and cancel the process
						if(!rdbtnApprentissage.isSelected() && !rdbtnK_fold.isSelected()) {
							JOptionPane.showMessageDialog(null, "Selectionez une option : Appretissage complet ou K-fold");
							return;
						}
						
						btnExport.setEnabled(false);
						lblTraitement.setVisible(true);
						lblTraitement.setText(lblEnCours);
						
						if(rdbtnK_fold.isSelected()) {							
							progressBar.setMinimum(0);
							progressBar.setMaximum((int)kspinner.getValue() * (int)itspinner.getValue());
							
							ExecutorService executor = Executors.newFixedThreadPool(1);
							Callable<ConfusionMatrix> thread = new ThreadLearnAndTest(txtSelectionezUnDossier.getText(),
									(int)kspinner.getValue(), 
									(int)nbspinner.getValue(),
									(int)itspinner.getValue(),
									(int)lenspinner.getValue(),
									Double.parseDouble(textField.getText()));
							
							matrix = executor.submit(thread);
							
							executor.shutdown();
							
							btnTraiterDoc.setEnabled(false);
						}
					}
				});
				horizontalBox.add(btnTraiterDoc);
		
		Component verticalStrut_6 = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut_6);
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		verticalBox.add(progressBar);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1);
		
		lblTraitement = new JLabel(lblEnCours);
		lblTraitement.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(lblTraitement);
		lblTraitement.setVisible(false);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut_2);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_2);
		
		lblPrecision = new JLabel("Precision : ");
		horizontalBox_2.add(lblPrecision);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalBox_2.add(horizontalStrut_1);
		
		lblRappel = new JLabel("Rappel : ");
		horizontalBox_2.add(lblRappel);
		
		Component verticalStrut_4 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_4);
		
		btnExport = new JButton("Export to CSV");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileSaver = new JFileChooser();
				fileSaver.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileSaver.setCurrentDirectory(new java.io.File("."));
				fileSaver.setDialogTitle("sauvegarder");
				fileSaver.setAcceptAllFileFilterUsed(false);
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
					String Path = fileChooser.getSelectedFile().toString()+"/confusionMatrix.CSV";
					try {
						Writer.writeTo(matrix.get().toString(), Path);
					} catch (InterruptedException | ExecutionException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnExport.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(btnExport);
		btnExport.setEnabled(false);
	}
	
	/**
	 * increment the progress bar by one unit 
	 */
	public static void incrementProgressBar() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(progressBar.getValue()+1 < progressBar.getMaximum()) {
					progressBar.setValue(progressBar.getValue()+1);
				}else {
					try {
						matrix.get().computeStats();
						NumberFormat formatter = new DecimalFormat("#0.00");
						progressBar.setValue(progressBar.getValue()+1);
						lblTraitement.setText("Terminé");
						lblPrecision.setText("Précision : " + formatter.format(100*matrix.get().getPrecision()) + " %");
						lblRappel.setText("Rappel : " + formatter.format(100*matrix.get().getRappel()) + " %");
						btnExport.setEnabled(true);
						btnTraiterDoc.setEnabled(true);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * get the progress bar, for thread utilities
	 * @return the progressBar
	 */
	public static JProgressBar getProgressBar() {
		return progressBar;
	}

}
