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
import java.io.File;
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

import resources.ResourcesLoader;

/**
 * Class that group other class to display the client view.
 * It allow picking files, start the processing, showing the progress, adding the results in separate tabs
 * @author axel
 */
public class ClientViewV2 extends JFrame {

	private static JTabbedPane tabbedPane;
	private static MainPane mainPane;
	
	private final String lblMenuFichier = "Fichier";
	private final String lblMenuOption = "Option";
	private final String lblMenuAide = "Aide";
	private final String lblMFichierSauvegarder = "sauvegarder";
	private final String lblMFichierFermer = "fermer";
	private final String lblMAideAPropos = "A Propos";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientViewV2 frame = new ClientViewV2();
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
	public ClientViewV2() {
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
		
		addPane("qfnq", new ArrayList<String>());
	}
	
	/**
	 * add a new closable tab to the main window
	 * It display a table with all pattern and their score 
	 * @param name the name of the pane
	 * @param results the data in the table
	 */
	public static void addPane(String name, List<String> results) {
		
		//add the pane to the tabbedPane
		tabbedPane.addTab(name, new ResultPane(results));
		
		//get the index of the inserted pane
		int index = tabbedPane.indexOfTab(name);
		
		//create a panel that contain a label and a click-able label 
		JPanel pnlTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(name);
		lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		pnlTab.add(lblTitle);
		
		JLabel lblClose = new JLabel("x");
		//add mouse listener to change color of the label on mouse over, close the pane on click
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent  e) {
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
		
		//add the panel onto the pane header
		tabbedPane.setTabComponentAt(index, pnlTab);
	}
}
