import java.util.Arrays;
import java.util.List;

class StochGDSVM extends Learner {
	List<Double> labList;
	List<Double[]> featList;
	
	StochGDSVM(LearnerData data) { 
		super(data);
		eta = 0.0001;
		epsilon = 0.001;
		C = 100;
		w = Arrays.stream(new double[feats.first().length]).mapToObj(x->x).toArray(Double[]::new);
		d = w.length;
	}
	
	boolean isConverged() {
		
		/*!!!!!*/
		return false;
	}
}