package main;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PDDocument pdfTest = null;
		
		try {
			pdfTest = PDDocument.load(new File("src/main/resources/pdf/AF MAINTENANCE/16904-ESA79087987.pdf"));
			
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);
			
			for (int p = 0; p < pdfTest.getNumberOfPages(); p++) {
				stripper.setStartPage(p);
				stripper.setEndPage(p);
				
				String text = stripper.getText(pdfTest);
				System.out.println(text.trim());
				System.out.println();
			}
		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
