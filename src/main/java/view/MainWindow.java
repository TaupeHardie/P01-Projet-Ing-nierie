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
import java.awt.Color;
import javax.swing.JProgressBar;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtSelectionezUnDossier;
	private DefaultTableModel dtm;
	private JTable table;

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
		
		JMenu mnFichier = new JMenu("Fichier");
		menuBar.add(mnFichier);
		
		JMenuItem mntmClose = new JMenuItem("fermer");
		mnFichier.add(mntmClose);
		
		JMenuItem mntmSauvegarder = new JMenuItem("sauvegarder");
		mnFichier.add(mntmSauvegarder);
		
		JMenu mnOption = new JMenu("Option");
		menuBar.add(mnOption);
		
		JMenu mnAide = new JMenu("Aide");
		menuBar.add(mnAide);
		
		JMenuItem mntmAPropos = new JMenuItem("A Propos");
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
		txtSelectionezUnDossier.setForeground(Color.LIGHT_GRAY);
		txtSelectionezUnDossier.setText("Selectionez un dossier ...");
		horizontalBox.add(txtSelectionezUnDossier);
		txtSelectionezUnDossier.setColumns(10);
		
		JButton btnTraiterDoc = new JButton("Traiter les documments");
		horizontalBox.add(btnTraiterDoc);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut);
		
		JProgressBar progressBar = new JProgressBar();
		verticalBox.add(progressBar);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1);
		
		JLabel lblTraitement = new JLabel("Veuillez patienter, traitement en cours ...");
		lblTraitement.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(lblTraitement);
		
		Component verticalStrut_2 = Box.createVerticalStrut(30);
		verticalBox.add(verticalStrut_2);
		
		JScrollPane scrollPane = new JScrollPane();
		verticalBox.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
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
		});
		scrollPane.setViewportView(table);
		
		dtm = new DefaultTableModel(); 
		Object[] titles = {"Pattern", "Score"};
        dtm.setColumnIdentifiers(titles);
        Object[] rowdata = {"1",6};
        dtm.addRow(rowdata);
	}

}
