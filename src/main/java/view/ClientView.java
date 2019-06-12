package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import apprentissage.Sortie;
import misc.Const;
import resources.ResourcesLoader;

/**
 * Classe qui regroupe plusieurs composants pour faire le vue client.
 * Cette vue permet de trouver le meilleur pattern pour les pdf selectionnes.
 * Les resulats sont affiches dans des onglets separes.
 */
public class ClientView extends JFrame {

	private static JTabbedPane tabbedPane;
	private static MainPane mainPane;
	
	private final String lblMenuFichier = "Fichier";
	private final String lblMenuOption = "Option";
	private final String lblMenuAide = "Aide";
	private final String lblMFichierSauvegarder = "sauvegarder";
	private final String lblMFichierFermer = "fermer";
	private final String lblMAideAPropos = "A Propos";
	
	//disable error from pdfBox
	static {
	    System.setProperty("org.apache.commons.logging.Log",
	                 "org.apache.commons.logging.impl.NoOpLog");
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		File f = new File(Const.MainPath);
		if(!f.exists()) {
			try {
				Files.createDirectories(f.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientView frame = new ClientView();
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
	public ClientView() {
		setTitle("Qweeby - Format auto");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 660, 458);
		
		//add the menu
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
		
		//add the tabbedPane to have multiple pane
		tabbedPane = new JTabbedPane();
		setContentPane(tabbedPane);
		
		//add a non-closable pane that start the process
		mainPane = new MainPane();
		tabbedPane.addTab("Accueil", mainPane);
		
	}
	
	/**
	 * Ajoute un nouvel onglet a la vue
	 * Cela montre le pdf avec les patterns et leurs scores respectifs. 
	 * @param name le nom de l'onglet 
	 * @param results les donnees a afficher
	 */
	public static void addPane(String name, List<Sortie> results) {
		
		//add the pane to the tabbedPane
		tabbedPane.addTab(name, new ResultPane(results));

		//create a panel that contain a label and a click-able label 
		JPanel pnlTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(name);
		lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		pnlTab.add(lblTitle);
		
		JLabel lblClose = new JLabel("x");
		lblClose.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
		//add mouse listener to change color of the label on mouse over, close the pane on click
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent  e) {
				//get the index of the inserted pane
				int index = tabbedPane.indexOfTab(name);
		        if (index != -1) {
		        	tabbedPane.remove(index);
		        }
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				lblClose.setForeground(Color.red);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				lblClose.setForeground(Color.black);
			}
		});
		pnlTab.add(lblClose);
		
		//get the index of the inserted pane
		int index = tabbedPane.indexOfTab(name);
		
		//add the panel onto the pane header
		tabbedPane.setTabComponentAt(index, pnlTab);
	}
}
