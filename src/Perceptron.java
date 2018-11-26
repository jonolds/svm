import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

class Perceptron extends Learner {
	int correct = 0;
	Perceptron(LearnerData data) {
		super(data);
		eta = 0.5;
		run();
	}
	
	void run() {
		w = Arrays.stream(new double[feats.first().length]).mapToObj(x->x).toArray(Double[]::new);
		d = w.length;
		int cur_ex = 0;
		
		List<Double[]> examples = feats.collect();
		List<Double> lab = labs.collect();
		
		while(!isConverged()) {
			Double[] ex = examples.get(cur_ex);
			double wdotx = dot(w, ex);
			double y = lab.get(cur_ex);
			
			if(Math.signum(wdotx) == y)
				correct++;
			else {
				correct = 0;
				updateWeights(Math.signum(y), ex);
			}
			cur_ex += (cur_ex == (n-1)) ? (-cur_ex) : 1;
		}
		System.out.println("final w: " + toStr(w));
	}
	
	void updateWeights(double sign, Double[] x) {
		Double[] oldW = copy(w);
		w = IntStream.range(0, d).mapToObj(z->oldW[z] + (x[z]*sign*eta)).toArray(Double[]::new);
	}
	
	boolean isConverged() { return correct >= n; }
}