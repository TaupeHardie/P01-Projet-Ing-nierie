package apprentissage;

import java.util.List;

import org.ejml.simple.SimpleMatrix;

import extraction.Feature;

public class PMC {

	final static int lenmat = 11;

	public static SimpleMatrix FeaturesToNeuron(List<Feature> Flist) {
		SimpleMatrix output = new SimpleMatrix(1, lenmat * 4);
		String cat = Flist.get(0).getType();
		int fInd = 0;
		for (int n = 0; n < 4; n++) {
			for (int j = 1 + lenmat * n; j < lenmat * (n + 1); j++) {

				if (cat != Flist.get(fInd).getType()) {
					output.set(0, j, -1);
				} else {
					output.set(0, n * lenmat, output.get(0, n * lenmat) + 1);
					output.set(0, j, Flist.get(fInd).getPos());
					fInd++;
				}
			}
			if (n < 3) {
				while (cat == Flist.get(fInd).getType()) {
					fInd++;
				}
				cat = Flist.get(fInd).getType();
			}
		}
		return output;

	}
}
