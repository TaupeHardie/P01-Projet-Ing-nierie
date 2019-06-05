package apprentissage;

import static org.junit.Assert.*;

import org.ejml.ops.EjmlUnitTests;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import main.App;
import resources.ResourcesLoader;

public class TestApprentissage {

	@Test
	public void testkfoldCR() {
		DataManager dm = new DataManager();
		String path = App.MainPath+"pdf";
		
		dm.kfoldCrossValidation(10, path);
		assertEquals(ResourcesLoader.loadDirectory(path).size() 
				- ResourcesLoader.loadDirectory(path+"/_IGNORE").size(),
				dm.getSampleNumber());
		
		dm.kfoldCrossValidation(5,path);
		assertEquals(ResourcesLoader.loadDirectory(path).size() 
				- ResourcesLoader.loadDirectory(path+"/_IGNORE").size(),
				dm.getSampleNumber());
		
		dm.kfoldCrossValidation(3,path);
		assertEquals(ResourcesLoader.loadDirectory(path).size() 
				- ResourcesLoader.loadDirectory(path+"/_IGNORE").size(),
				dm.getSampleNumber());
	}
	
	@Test
	public void testResultMatrix() {
		String path = App.MainPath+"pdf";
		
		DataManager dm = new DataManager();
		dm.kfoldCrossValidation(10,path);

		SimpleMatrix test1 = new SimpleMatrix(11,1);
		test1.set(6,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(0).get(10), path).isIdentical(test1,1e-5) );
		
		SimpleMatrix test2 = new SimpleMatrix(11,1);
		test2.set(7,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(1).get(12),path).isIdentical( test2,1e-5));
		
		SimpleMatrix test3 = new SimpleMatrix(11,1);
		test3.set(9,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(5).get(15),path).isIdentical(test3, 1e-5));
		
		SimpleMatrix test4 = new SimpleMatrix(11,1);
		test4.set(2,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(1).get(4),path).isIdentical(test4, 1e-5));
		
		SimpleMatrix test5 = new SimpleMatrix(11,1);
		test5.set(6,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(6).get(10),path).isIdentical(test5, 1e-5));
		
		
		SimpleMatrix test6 = new SimpleMatrix(11,1);
		test6.set(1,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(7).get(2),path).isIdentical(test6, 1e-5) );
		
		SimpleMatrix test7 = new SimpleMatrix(11,1);
		test7.set(0,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(5).get(0),path).isIdentical(test7, 1e-5));
		
		SimpleMatrix test8 = new SimpleMatrix(11,1);
		test8.set(9,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(3).get(16),path).isIdentical(test8, 1e-5));
		

	}
	
	@Test
	public void testConfusionMatrix() {
		ConfusionMatrix matrix = new ConfusionMatrix(2, "");
		
		for(int i = 0; i < 95; i++)
			matrix.increment(0, 0);
		
		for(int i = 0; i < 5; i++)
			matrix.increment(0, 1);
		
		for(int i = 0; i < 3; i++)
			matrix.increment(1, 0);
		
		for(int i = 0; i < 97; i++)
			matrix.increment(1, 1);
		
		matrix.computeStats();
		
		assertEquals((95/98. + 97/102.)/2., matrix.getPrecision(), 1e-3);
		assertEquals((95/100. + 97/100.)/2., matrix.getRappel(), 1e-3);
		
	}

}
