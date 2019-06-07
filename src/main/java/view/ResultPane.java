package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import apprentissage.Sortie;



/**
 * class that display a scrollable table
 * @author axel
 */
public class ResultPane extends JScrollPane {
	private JTable table;
	private DefaultTableModel dtm;
	
	/**
	 * Create the panel.
	 */
	public ResultPane(List<Sortie> results) {
		
		setViewportBorder(null);
		//setPreferredSize(new Dimension(verticalBox.getWidth()-10, 250));
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
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.setDefaultRenderer(String.class, centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		
		setViewportView(table);
		
		for(Sortie s : results) {
			dtm.addRow(new Object[]{s.patternName, s.score});
		}
	}
}
