import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.spark.api.java.JavaDoubleRDD;

class BatchGDSVM extends Learner {
	Double[] oldW;
	double cost, oldCost;
	List<Double> labList;
	List<Double[]> featList;
	
	BatchGDSVM(LearnerData data) { 
		super(data);
		eta = .0000003; epsilon = 0.25; C = 100; b = 0.0;
		w = Arrays.stream(new double[feats.first().length]).mapToObj(x->x).toArray(Double[]::new);
		oldW = copy(w);
		d = w.length;
		run();
	}
	
	void run() {
		labList = labs.collect();
		featList = feats.collect();
		int k = 0;
		cost = updateCost();
		while(true) {
			oldCost = cost;
			IntStream.range(0, d).forEach(j->updateJthW(j));
			b = updateB();
			k+=1;
			cost = updateCost();
			if(isConverged() || k > 300)
				break;
		}
		print(k);
	}
	
	double updateB() {
		final Double[] wFinal = copy(w);
		final double bFinal = b;
		Double sum = train.mapToDouble(x->(x._2*(dot(x._1, wFinal)+bFinal) >= 1) ? 0 : -x._2).sum();
		return b - (eta*C*sum);
	}
	
	void updateJthW(int j) {
		w[j] = w[j] - eta*(grWRTj(j));
	}
	
	double grWRTj(int j) {
		final Double[] wFinal = copy(w); final double bFinal = b;
		double d = train.mapToDouble(x->x._2*(dot(x._1, wFinal) + bFinal) >= 1 ? 0 : -x._2*x._1[j]).sum();
		return w[j] + C*d;
	}	
	
	double updateCost() {
		double sum1 = .5*IntStream.range(0, d).mapToDouble(j->Math.pow(w[j], 2)).sum();
		double sum2 = IntStream.range(0, n).mapToDouble(z->Math.max(0, 1-labList.get(z)*(IntStream.range(0, d).mapToDouble(j->w[j]*featList.get(z)[j]).sum() + b))).sum();
		return sum1 + C*sum2;
	}
	
//	double innerCost(Double[] feat) {
//		final Double[] wFinal = copy(w); final double bFinal = b;
//		
//	}

	boolean isConverged() {
		return (Math.abs(oldCost - cost)*100)/oldCost < epsilon ? true : false;
	}
}