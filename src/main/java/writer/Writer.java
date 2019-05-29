package writer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Writer {
	
	/**
	 * efface le fichier specifié et le re-cree pour le remplir avec le texte specifié
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
}
