package apprentissage;

import static org.junit.Assert.*;

import org.ejml.ops.EjmlUnitTests;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import misc.Const;
import resources.ResourcesLoader;

public class TestApprentissage {
	
	static {
	    System.setProperty("org.apache.commons.logging.Log",
	                 "org.apache.commons.logging.impl.NoOpLog");
	}

	@Test
	public void testkfoldCR() {
		String path = Const.MainPath+"pdf";
		DataManager dm = new DataManager(path);
		
		
		dm.kfoldCrossValidation(10);
		assertEquals(ResourcesLoader.loadFileIn(path).size() 
				- ResourcesLoader.loadFileIn(path+"/_IGNORE").size(),
				dm.getSampleNumber());
		
		dm.kfoldCrossValidation(5);
		assertEquals(ResourcesLoader.loadFileIn(path).size() 
				- ResourcesLoader.loadFileIn(path+"/_IGNORE").size(),
				dm.getSampleNumber());
		
		dm.kfoldCrossValidation(3);
		assertEquals(ResourcesLoader.loadFileIn(path).size() 
				- ResourcesLoader.loadFileIn(path+"/_IGNORE").size(),
				dm.getSampleNumber());
	}
	
	@Test
	public void testResultMatrix() {
		String path = Const.MainPath+"pdf";
		
		DataManager dm = new DataManager(path);
		dm.kfoldCrossValidation(10);

		SimpleMatrix test1 = new SimpleMatrix(11,1);
		test1.set(6,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(0).get(10)).isIdentical(test1,1e-5) );
		
		SimpleMatrix test2 = new SimpleMatrix(11,1);
		test2.set(7,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(1).get(12)).isIdentical( test2,1e-5));
		
		SimpleMatrix test3 = new SimpleMatrix(11,1);
		test3.set(9,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(5).get(15)).isIdentical(test3, 1e-5));
		
		SimpleMatrix test4 = new SimpleMatrix(11,1);
		test4.set(2,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(1).get(4)).isIdentical(test4, 1e-5));
		
		SimpleMatrix test5 = new SimpleMatrix(11,1);
		test5.set(6,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(6).get(10)).isIdentical(test5, 1e-5));
		
		
		SimpleMatrix test6 = new SimpleMatrix(11,1);
		test6.set(1,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(7).get(2)).isIdentical(test6, 1e-5) );
		
		SimpleMatrix test7 = new SimpleMatrix(11,1);
		test7.set(0,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(5).get(0)).isIdentical(test7, 1e-5));
		
		SimpleMatrix test8 = new SimpleMatrix(11,1);
		test8.set(9,1.0);
		assertTrue(PMC.getExpectedResultsMatrix(dm.getData().get(3).get(16)).isIdentical(test8, 1e-5));
		

	}
	
	@Test
	public void testConfusionMatrix() {
		ConfusionMatrix matrix = new ConfusionMatrix(2);
		
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
	
	@Test
	public void testSauvegardeLoadMatrix() {
		PMC pmc = new PMC(Const.MainPath+"pdf", 10, 50, 50, 10, 0.002);
		
		pmc.learnAndTestThread();
				
		SimpleMatrix test1=pmc.getW();
		
		pmc.saveWeightMatrix();
		SimpleMatrix test2= new SimpleMatrix(50,45);
		pmc.setW(test2);
		
		pmc.loadWeightMatrix();
		
		
		assertTrue(test1.isIdentical(pmc.getW(),1e-5) );

	}

}
