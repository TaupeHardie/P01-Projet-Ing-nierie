package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import extraction.Feature;
import extraction.Regexp;

/**
 * Classe representant un fichier PDF
 *
 */
public class PDF {
	/**
	 * Objet PDDocument representant le fichier PDF
	 */
	private PDDocument doc;
	private String name ="";
	private List<Feature> features =null;
	
	/**
	 * Constructeur par defaut
	 */
	public PDF() {
		super();
	}

	/**
	 * Constructeur. Charge le fichier PDF a� partir d'un chemin
	 * @param fileName Chaine de caractere vers le chemin du fichier a� ouvrir
	 */
	public PDF(String fileName) {
		super();
		loadPDF(fileName);
		compute();
		this.name = fileName;
	}
	
	/**
	 * Constructeur. Charge le fichier PDF a� partir d'un objet
	 * @param file Objet correspondant au PDF a charger
	 */
	public PDF(File file) {
		super();
		loadPDF(file);
		compute();
		this.name = file.getPath();
	}
	
	/**
	 * getter pour avoir le nom du pdf
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Charge un PDF a partir du chemin
	 * @param fileName Chemin du PDF
	 */
	public void loadPDF(String fileName) {
		try {
			doc = PDDocument.load(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Charge un PDF a partir d'un objet File
	 * @param file Objet correspondant au PDF a charger
	 */
	public void loadPDF(File file) {
		try {
			doc = PDDocument.load(file);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * extract the feature from the pdf and close the document
	 */
	public void compute() {
		findMatches(convertToText());
		close();
	}
	
	/**
	 * Convertit le contenu du PDF en texte brut
	 */
	public String convertToText() {
		PDFTextStripper stripper;
		String content = "";
		try {
			stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);
			
			for (int p = 0; p < doc.getNumberOfPages() + 1; p++) {
				stripper.setStartPage(p);
				stripper.setEndPage(p);
				
				String text = stripper.getText(doc);
				content += text.trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * Ferme le doucment
	 */
	public void close() {
		try {
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recherche toutes les occurences de dates, d'addresses, de code et de prix dans le pdf
	 */
	public void findMatches(String content) {
		features = Regexp.getAllFeatures(content);
	}
	
	/**
	 * Retourne toutes les occurences de dates, d'addresses, de code et de prix dans le pdf
	 */
	public List<Feature> getFeatures() {
		return features;
	}
	
	

}
