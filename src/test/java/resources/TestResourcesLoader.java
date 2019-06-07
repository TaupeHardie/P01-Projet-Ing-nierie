package resources;

import static org.junit.Assert.*;

import org.junit.Test;

import misc.Const;

public class TestResourcesLoader {

	@Test
	public void test() {
		ResourcesLoader.loadResourcesIn(Const.MainPath+"pdf");
		assertEquals(ResourcesLoader.loadFileIn(Const.MainPath+"pdf").size(), ResourcesLoader.getPDFs().size());
	}

}
