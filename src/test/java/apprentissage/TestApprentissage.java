package apprentissage;

import static org.junit.Assert.*;

import org.junit.Test;

import resources.ResourcesLoader;

public class TestApprentissage {

	@Test
	public void testkfoldCR() {
		DataManager dm = new DataManager();
		
		dm.kfoldCrossValidation(10);
		assertEquals(ResourcesLoader.loadDirectory("src/main/resources/pdf").size() 
				- ResourcesLoader.loadDirectory("src/main/resources/pdf/_IGNORE").size(),
				dm.getSampleNumber());
		
		dm.kfoldCrossValidation(5);
		assertEquals(ResourcesLoader.loadDirectory("src/main/resources/pdf").size() 
				- ResourcesLoader.loadDirectory("src/main/resources/pdf/_IGNORE").size(),
				dm.getSampleNumber());
		
		dm.kfoldCrossValidation(3);
		assertEquals(ResourcesLoader.loadDirectory("src/main/resources/pdf").size() 
				- ResourcesLoader.loadDirectory("src/main/resources/pdf/_IGNORE").size(),
				dm.getSampleNumber());
	}

}
