package apprentissage;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import extraction.Feature;

import org.ejml.ops.EjmlUnitTests;
import org.ejml.simple.SimpleMatrix;
public class TestPMCFunct {

	@Test
	public void testFeaturesToNeuron() {
		List<Feature> Flist=new ArrayList<Feature>();
		Flist.add(new Feature(27, "15,20 €", "FeaturePrix"));
		Flist.add(new Feature(0, "10/12/2017", "FeatureDate"));
		Flist.add(new Feature(0, "10/12/2017", "FeatureDate"));
		Flist.add(new Feature(0, "10/12/2017", "FeatureDate"));
		Flist.add(new Feature(0, "15 rue du poulet frit", "FeatureAddresse"));
		Flist.add(new Feature(0, "15 rue du poulet frit", "FeatureAddresse"));
		Flist.add(new Feature(0, "15 rue du poulet frit", "FeatureAddresse"));
		Flist.add(new Feature(0, "15 rue du poulet frit", "FeatureAddresse"));
		Flist.add(new Feature(0, "15 rue du poulet frit", "FeatureAddresse"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		Flist.add(new Feature(5, "FR201536F7", "FeatureCode"));
		double[][] data= {{1,27,-1,-1,-1,-1,-1,-1,-1,-1,-1,3,0,0,0,-1,-1,-1,-1,-1,-1,-1,5,0,0,0,0,0,-1,-1,-1,-1,-1,10,5,5,5,5,5,5,5,5,5,5}};
		SimpleMatrix expected= new SimpleMatrix(data);
		assertTrue(expected.isIdentical(PMC.FeaturesToNeuron(Flist), 1e-5));
		
	}

}
