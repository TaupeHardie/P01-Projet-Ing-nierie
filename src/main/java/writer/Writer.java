package writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import extraction.Feature;
import misc.Const;
import misc.PDF;

/**
 * Classe statique permetant de gerer les flux de sorties
 */
public class Writer {
	
	/**
	 * efface le fichier specifie et le re-cree pour le remplir avec le texte specifie
	 * @param texte le texte a ecrire
	 * @param fileName le fichier ou ecrire
	 */
	public static void writeTo(String texte, String fileName) {
		File f = new File(fileName);
		if(f.exists()) {
			f.delete();
		}
		
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Files.write(f.toPath(), texte.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void clearFile(String filename) {
		File f = new File(filename);
		if(f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

 	public static void appendPdfTo(PDF pdf, String filename) {
		File f = new File(filename);
		if(f.exists()) {
			BufferedWriter writer = null;
			try {
				writer = Files.newBufferedWriter(f.toPath(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
				writer.newLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				writer.write(pdf.getName());
				writer.newLine();
				for(Feature feature : pdf.getFeatures()) {
					writer.write(feature.getPos()+Const.PosDelimiter+feature.getStr()+Const.StrDelimiter+feature.getType());
					writer.newLine();
				}
				writer.write(Const.PdfDelimiter);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
