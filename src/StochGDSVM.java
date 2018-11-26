import java.util.List;
import java.util.Random;

import scala.Tuple2;

@SuppressWarnings("static-access")
class StochGDSVM extends LearnerHelp {
	Random r = new Random();
	static List<Double> yL;
	static List<Double[]> xL;
	static List<Tuple2<Double[],Double>> tL;
	
	static int n, d, C = 100;
	static double eta, epsilon, cost, costOLD, b, bOLD;
	static double[] w, wOLD;

	StochGDSVM(LearnerData data) { 
		this.xL = data.fL;
		this.yL = data.lL;
		this.tL = data.tL;

		n = tL.size();
		d = xL.get(0).length;
		eta = 0.0001; epsilon = 0.001;
		w = new double[d];
		run();
	}
	
	void run() {
		int k = 0;
		cost = 0.0;
		while(true) {
			costOLD = cost;
			wOLD = copy(w);
			bOLD = b;
			
			final int i = r.nextInt(n);
			final Double[] xi = xL.get(i);
			final double yi = yL.get(i);
			
			ints(0, d).mapToDouble(j->(w[j] = updateJthW(j)));

			b = getNewB(xi, yi);
			cost = getCost(xi, yi);
			
//			System.out.println(cost);
			k+=1;
			if(isConverged() || k > 10000)
				break;
		}
		print(k);
	}

	
	double getFWB() {
		double sumL = .5*ints(0, d).mapToDouble(j->Math.pow(w[j], 2)).sum();
		double sumR = C*ints(0, n).mapToDouble(i->Math.max(0, 1-yL.get(i)*(dotProduct(w, xL.get(i))+b))).sum();
		return sumL + sumR;
	}
	
	double getNewB(Double[] xi, double yi) {
		double nextB;
		if(yi*(dotProduct(w, xi) + b) < 1)
			nextB = C*(-yi);
		else
			nextB = 0;
		return b-eta*(nextB);
	}
	
	double updateJthW(int j) {
		double sum1 = tL.stream().mapToDouble(t->t._2*(dotProduct(w, t._1) + b) >= 1 ? 0 : -t._2*t._1[j]).sum();
		sum1 = w[j] + C*sum1;
		return w[j] - eta*(sum1);
	}
	
	double getCost(Double[] xi, double yi) {
//		Double[] xi = xL.get(i);
//		double yi = yL.get(i);
		
		double sumL = .5*ints(0, d).mapToDouble(j->Math.pow(w[j], 2)).sum();
		double sumR = C*Math.max(0, 1-yi*(dotProduct(w, xi)+b));
		return sumL + sumR;
	}
	
	double getCostPercentChange() {
		return (Math.abs(costOLD - cost)*100)/costOLD;
	}

	boolean isConverged() {
		double costK = .5*costOLD + .5*getCostPercentChange();
		System.out.println("costK: " + costK + "   eps: " + epsilon + "   cost < eps: " + (cost < epsilon) + "    cost: " + cost);
		return costK < epsilon ? true : false;
	}
}


////Computes the stochast cost
//public double kCostJON(int i){
//	return .5*costOLD + .5*percentCostJON(i);
//}
////Computes percent cost
//public double percentCostJON(int i){
//	double prevFKB = computeFWBJON(wOLD, bOLD);
//	double newFKB = computeFWBJON(w, b);
//	return (Math.abs(prevFKB - newFKB)*100)/prevFKB;
//}
////Computes f(w,b)
//public double computeFWBJON(double[]wi, double bi){
//	double squaredSum = 0.0;
//	for(int i = 0; i < wi.length; i++){
//		squaredSum = squaredSum + wi[i]*wi[i];
//	}
//	
//	squaredSum = squaredSum/2;
//	double rightSum = 0.0;
//	for(int i = 0; i < n; i++){
//		double yi = yL.get(i);
//		double sum = dotProduct(wi, xL.get(i)) + bi;
//		double product = yi*sum;
//		rightSum = rightSum + Math.max(0,1-product);
//	}
//	return squaredSum + C*rightSum;
//}