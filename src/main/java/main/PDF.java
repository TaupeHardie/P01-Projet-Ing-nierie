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
 * Classe représentant un fichier PDF
 *
 */
public class PDF {
	/**
	 * Objet PDDocument représentant le fichier PDF
	 */
	private PDDocument doc;
	private String content = "";
	
	/**
	 * liste des feature detectées dans le pdf
	 */
	private List<Feature> features = new ArrayList<Feature>();
	
	/**
	 * Constructeur par défaut
	 */
	public PDF() {
		super();
	}

	/**
	 * Constructeur. Charge le fichier PDF à partir d'un chemin
	 * @param fileName Chaine de caractère vers le chemin du fichier à ouvrir
	 */
	public PDF(String fileName) {
		super();
		loadPDFfromString(fileName);
	}
	
	/**
	 * Constructeur. Charge le fichier PDF à partir d'un objet
	 * @param file Objet correspondant au PDF à charger
	 */
	public PDF(File file) {
		super();
		loadPDFfromFile(file);
	}
	
	/**
	 * Charge un PDF à partir du chemin
	 * @param fileName Chemin du PDF
	 */
	public void loadPDFfromString(String fileName) {
		try {
			doc = PDDocument.load(new File(fileName));
			convertToText();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Charge un PDF à partir d'un objet File
	 * @param file Objet correspondant au PDF à charger
	 */
	public void loadPDFfromFile(File file) {
		try {
			doc = PDDocument.load(file);
			convertToText();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Convertit le contenu du PDF en texte brut
	 */
	public void convertToText() {
		PDFTextStripper stripper;
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
	}
	
	@Override
	public String toString() {
		return "PDF [content=" + content + "]";
	}

	/**
	 * Retourne le contenu du document
	 * @return Contenu du PDF en texte
	 */
	public String getText() {
		return content;
	}
	
	/**
	 * Recherche toutes les occurences de dates, d'addresses, de code et de prix dans le pdf
	 */
	public List<Feature> findMatches() {
		features = Regexp.getAllFeatures(content);
		return features;
	}

	
	

}
