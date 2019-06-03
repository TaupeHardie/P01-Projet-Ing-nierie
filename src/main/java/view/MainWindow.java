package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import javax.swing.JProgressBar;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import resources.ResourcesLoader;

import javax.swing.JScrollPane;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * GUI made with window builder
 * @author axel
 *
 */
public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtSelectionezUnDossier;
	private DefaultTableModel dtm;
	private JTable table;
	private JFileChooser fileChooser;
	private JLabel lblTraitement;
	private static JProgressBar progressBar;
	
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
					MainWindow frame = new MainWindow();
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
	public MainWindow() {
		setTitle("Qweeby - Format auto");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 660, 458);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu(lblMenuFichier);
		menuBar.add(mnFichier);
		
		JMenuItem mntmClose = new JMenuItem(lblMFichierFermer);
		mnFichier.add(mntmClose);
		
		JMenuItem mntmSauvegarder = new JMenuItem(lblMFichierSauvegarder);
		mnFichier.add(mntmSauvegarder);
		
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
				if(txtSelectionezUnDossier.getText().equalsIgnoreCase(lblSelectDir)) {
					txtSelectionezUnDossier.dispatchEvent(new MouseEvent(txtSelectionezUnDossier, MouseEvent.MOUSE_CLICKED, 0, 0, 100, 100, 1, false));
				}
				lblTraitement.setVisible(true);
				lblTraitement.setText(lblEnCours);
				
				//get all the file to be processed
				List<File> lst = ResourcesLoader.loadInput(txtSelectionezUnDossier.getText());
				
				progressBar.setMinimum(0);
				progressBar.setMaximum(lst.size());
				
				//launch the logic in a new thread here
				
				//fill the Jtable with result
				lblTraitement.setText(lblTermine);
				List<String> results = new ArrayList<String>();
				results.add("best pattern");
				results.add("good pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("correct pattern");
				results.add("poop pattern");
				for(String s : results) {
					dtm.addRow(new Object[]{s,1});
				}
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
		
		Component verticalStrut_2 = Box.createVerticalStrut(30);
		verticalBox.add(verticalStrut_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setPreferredSize(new Dimension(verticalBox.getWidth()-10, 250));
		verticalBox.add(scrollPane);
		
		table = new JTable();
		dtm = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Pattern", "Score"
				}
			) {
				Class[] columnTypes = new Class[] {
					String.class, Integer.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			};
		table.setModel(dtm);
		scrollPane.setViewportView(table);
	}
	
	public static void incrementProgressBar() {
		if(progressBar.getValue() < progressBar.getMaximum()) {
			progressBar.setValue(progressBar.getValue()+1);
		}
	}
	
	public static JProgressBar getProgressBar() {
		return progressBar;
	}

}
