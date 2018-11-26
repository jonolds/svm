import java.util.List;
import java.util.stream.IntStream;

import scala.Tuple2;

class Perceptron extends LearnerHelp {
	int correct = 0;
	List<Double> yL;
	List<Double[]> xL;
	List<Tuple2<Double[],Double>> tL;
	
	int n, d, C = 100;
	double eta, epsilon, b, bOLD, cost, costOLD;
	double[] w, wOLD;
	
	Perceptron(LearnerData data) {
		this.xL = data.fL;
		this.yL = data.lL;
		this.tL = data.tL;

		n = tL.size();
		d = xL.get(0).length;
		w = new double[d];
		wOLD = copy(w);
		eta = 0.5;
		run();
	}
	
	void run() {
		w = new double[d];
		int cur_ex = 0;
		
		while(!isConverged()) {
			Double[] ex = xL.get(cur_ex);
			double wdotx = dotProduct(w, ex);
			double y = yL.get(cur_ex);
			
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
		double[] oldW = copy(w);
		w = IntStream.range(0, d).mapToDouble(z->oldW[z] + (x[z]*sign*eta)).toArray();
	}
	
	boolean isConverged() { return correct >= n; }
}