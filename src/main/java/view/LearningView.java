package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.ejml.simple.SimpleMatrix;

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
import javax.swing.JProgressBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

/**
 * Gui for QWEEBY, It process pdf to train the algorithm 
 * @author axel
 */
public class LearningView extends JFrame {

	private JPanel contentPane;
	private JTextField txtSelectionezUnDossier;
	private JFileChooser fileChooser;
	private JFileChooser fileSaver;
	private JLabel lblTraitement;
	private static JProgressBar progressBar;
	private JButton btnExport;
	private JRadioButton rdbtnApprentissage;
	private JRadioButton rdbtnK_fold;
	private SimpleMatrix matrix = new SimpleMatrix() {
		@Override
		public String toString() {
			//custom toString to print the header (name of the class) of the table
			List<String> dirNames = ResourcesLoader.getDirectoriesName(txtSelectionezUnDossier.getText());
			StringBuilder rtn = new StringBuilder();
			
			for(String dirName : dirNames) {
				rtn.append(";"+dirName);
			}
			rtn.append("\n");
			for(int l =0; l< this.numRows();l++) {
				rtn.append(dirNames.get(l));
				for(int c =0; c< this.numCols();c++) {
					rtn.append(";"+this.get(l, c));
				}
			}
			return rtn.toString();
		}
	};
	
	private final String lblEnCours = "Veuillez patienter, traitement en cours ...";
	private final String lblTermine = "Terminé";
	private final String lblSelectDir = "selectionnez un dossier ...";
	private final String lblBtnTraiter = "Traiter les documments";
	private final String lblMenuFichier = "Fichier";
	private final String lblMenuOption = "Option";
	private final String lblMenuAide = "Aide";
	private final String lblMFichierSauvegarder = "sauvegarder";
	private final String lblMFichierFermer = "fermer";
	private final String lblMAideAPropos = "A Propos";
	private final String lblFileChooser = "selectionnez un dossier";

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
		setBounds(100, 100, 667, 267);
		
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
				fileChooser.setCurrentDirectory(new java.io.File("."));
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
		
		
		JButton btnTraiterDoc = new JButton(lblBtnTraiter);
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
				
				//get all the file to be processed
				List<File> lst = ResourcesLoader.loadInput(txtSelectionezUnDossier.getText());
				
				progressBar.setMinimum(0);
				progressBar.setMaximum(lst.size());
				
				//launch the logic in a new thread here
				//SimpleMatrix matrixNaive = 
				//matrix.set(matrixNaive);
				
				//end task
				btnExport.setEnabled(true);
				lblTraitement.setText(lblTermine);
			}
		});
		horizontalBox.add(btnTraiterDoc);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		verticalBox.add(horizontalBox_1);
		
		rdbtnApprentissage = new JRadioButton("Apprentissage complet");
		horizontalBox_1.add(rdbtnApprentissage);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		horizontalBox_1.add(horizontalStrut);
		
		rdbtnK_fold = new JRadioButton("K-fold");
		horizontalBox_1.add(rdbtnK_fold);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnApprentissage);
		group.add(rdbtnK_fold);
		
		Component verticalStrut_3 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_3);
		
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
		
		JLabel lblPrecision = new JLabel("Precision : ");
		horizontalBox_2.add(lblPrecision);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalBox_2.add(horizontalStrut_1);
		
		JLabel lblRappel = new JLabel("Rappel : ");
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
					Writer.writeTo(matrix.toString(), Path);
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
		if(progressBar.getValue() < progressBar.getMaximum()) {
			progressBar.setValue(progressBar.getValue()+1);
		}
	}
	
	/**
	 * get the progress bar, for thread utilities
	 * @return the progressBar
	 */
	public static JProgressBar getProgressBar() {
		return progressBar;
	}

}
