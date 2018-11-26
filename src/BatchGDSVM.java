import java.util.List;

import scala.Tuple2;

@SuppressWarnings("static-access")
class BatchGDSVM extends LearnerHelp {
	static List<Double> yL;						//Labels
	static List<Double[]> xL;					//training Points (features)
	static List<Tuple2<Double[],Double>> tL;	//Labels and Points combined
	
	static int n, d, C = 100;
	static double eta, epsilon, b, bOLD, cost, costOLD;
	static double[] w, wOLD;
	
	BatchGDSVM(LearnerData data) { 
		this.xL = data.fL; this.yL = data.lL; this.tL = data.tL;

		n = tL.size(); d = xL.get(0).length; w = new double[d];
		wOLD = copy(w);
		eta = .0000003; epsilon = 0.25;
		run();
	}
	
	void run() {
		int k = 0;
		cost = getNewCost();
		while(true) {
			costOLD = cost;
			for(int j = 0; j < d; j++)
				w[j] = getNewJthW(j);
			b = getNewB();
			k+=1;
			if(k%10 == 0) println(k);
			cost = getNewCost();
			if(isConverged() || k > 300)
				break;
		}
		print(k);
	}
	
	double getNewB() {
		Double sum = tL.stream().mapToDouble(x->(x._2*(dotProduct(w, x._1)+b) >= 1) ? 0 : -x._2).sum();
		return b - (eta*C*sum);
	}
	
	double getNewJthW(int j) {
		double sum1 = tL.stream().mapToDouble(t->t._2*(dotProduct(w, t._1) + b) >= 1 ? 0 : -t._2*t._1[j]).sum();
		sum1 = w[j] + C*sum1;
		return w[j] - eta*(sum1);
	}	
	
	double getNewCost() {
		double sumL = ints(0, 122).mapToDouble(j->Math.pow(w[j], 2)).sum();
		double sumR = tL.stream().mapToDouble(t->Math.max(0, 1-t._2*(dotProduct(w, t._1) + b))).sum();
		return .5*sumL + C*sumR;
	}
	
	double getCostPercentChange() {
		return (Math.abs(costOLD - cost)*100)/costOLD;
	}

	boolean isConverged() {
		double costPercentChange = getCostPercentChange();
		System.out.println("costPercentChange: " + costPercentChange + "   eps: " + epsilon + "   costPercentChange < eps: " + (costPercentChange < epsilon));
		return getCostPercentChange() < epsilon ? true : false;
	}
}