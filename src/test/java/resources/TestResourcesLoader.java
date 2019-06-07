package resources;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import misc.Const;
import writer.Writer;

public class TestResourcesLoader {

//	@Test
//	public void test() {
//		ResourcesLoader.loadResourcesIn(Const.MainPath+"pdf");
//		assertEquals(ResourcesLoader.loadFileIn(Const.MainPath+"pdf").size(), ResourcesLoader.getPDFs().size());
//	}
	
	@Test
	public void testRead() {
		String text = "pdf1\n pdf2\n pdf3";
		Writer.writeTo(text, Const.MainPath+"input.txt");
		List<String> lst = ResourcesLoader.readFile(Const.MainPath+"input.txt");
		
		assertEquals("pdf1", lst.get(0));
		assertEquals("pdf2", lst.get(0));
		assertEquals("pdf3", lst.get(0));
		
	}

}
