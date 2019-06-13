package reader;

 import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

 import extraction.Feature;
import misc.Const;
import misc.PDF;

 public class Reader {
	private static FileInputStream fis = null;
	private static InputStreamReader isr = null;
	private static BufferedReader buff = null;

 	 /**
     * lit un fichier et retourne le texte
     * @param fileName le fichier a lire
     * @return une liste de ligne
     */
    public static List<String> readFile(String fileName) {
    	List<String> output = new ArrayList<String>();
    	File f = new File(fileName);
    	openFile(f);

 		String ligne = null;
		try {
			ligne = buff.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (ligne != null) {
			output.add(ligne);
			try {
				ligne = buff.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		close();

 		return output;
	}


     public static PDF getPdfNamed(String name, String fileName) {
    	List<Feature> features = new ArrayList<Feature>();

     	File f = new File(fileName);
    	openFile(f);
    	String ligne = null;
		try {
			ligne = buff.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (ligne != null && !ligne.equalsIgnoreCase(name)) {
			try {
				ligne = buff.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			ligne = buff.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(ligne != null && !ligne.equalsIgnoreCase(Const.PdfDelimiter)){
			int pos = Integer.valueOf(ligne.substring(0, ligne.indexOf(Const.PosDelimiter)));
			String str = ligne.substring(ligne.indexOf(Const.PosDelimiter)+1, ligne.indexOf(Const.StrDelimiter));
			String type = ligne.substring(ligne.indexOf(Const.StrDelimiter)+1);
			Feature feat = new Feature(pos, str, type);
			features.add(feat);
			try {
				ligne = buff.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		PDF p = new PDF(name, features);
    	close();
    	return p;
    }

     /**
     * ouvre les flux pour lire le fichier
     * @param f le fichier a lire
     */
    private static void openFile(File f) {
    	try {
			 fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

 		isr = new InputStreamReader(fis);
		buff = new BufferedReader(isr);
    }

     /**
     * ferme les flux
     */
    private static void close() {
    	try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			isr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			buff.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


 }