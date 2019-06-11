package resources;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import misc.Const;
import writer.Writer;

public class TestResourcesLoader {

	@Test
	public void test() {
		ResourcesLoader.loadResourcesIn(Const.MainPath+"pdf");
		assertEquals(ResourcesLoader.loadFileIn(Const.MainPath+"pdf").size(), ResourcesLoader.getPDFs().size());
	}
	
	@Test
	public void testRead() {
		List<String> lst = ResourcesLoader.readFile(Const.MainPath+"input.txt");
		
		assertEquals("aaa", lst.get(0));
		assertEquals("bbb", lst.get(1));
		assertEquals("ccc", lst.get(2));
		
	}

}
