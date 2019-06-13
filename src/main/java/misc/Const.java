package misc;

/**
 * Class stockant toutes les valeurs constante de l'application
 */
public class Const {
	public static final String MainPath = System.getProperty("user.home")+"\\AppData\\Local\\Qweeby\\";
	public static final String DesktopPath = System.getProperty("user.home")+"\\Desktop";
	public static final int nbCore = Runtime.getRuntime().availableProcessors();
	public static String WorkingDir = "";
	public static final String StorePath = System.getProperty("user.home")+"\\AppData\\Local\\Qweeby\\pdfs.txt";
	public static final String PdfDelimiter = "fffffffffffffffffffff";
	public static final String PosDelimiter = "-p-";
	public static final String StrDelimiter = "-s-";
}
