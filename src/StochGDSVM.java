import java.util.List;
import java.util.Random;

import scala.Tuple2;

class StochGDSVM extends LearnerHelp {
	Random r;
	List<Double> yL;
	List<Double[]> xL;
	List<Tuple2<Double[],Double>> tL;
	
	int n, d, C = 100, k = 0;
	double eta, epsilon, cost, b, bOLD;
	double[] w, wOLD;

	StochGDSVM(LearnerData data) { 
		this.xL = data.fL; this.yL = data.lL; this.tL = data.tL;
		r = new Random();
		
		k = 0; n = tL.size(); d = xL.get(0).length;
		eta = 0.0001; epsilon = 0.001;
		w = new double[d];
		run();
	}
	
	void run() {
		cost = getCost(w, b);
		while(true) {
			bOLD = b; wOLD = copy(w);
			final int i = r.nextInt(n);
			w = getNewW(xL.get(i), yL.get(i));
			b = getNewB(xL.get(i), yL.get(i));
//			cost = getCost();
			k+=1;
			if(isConverged() || k > 10000)
				break;
		}
		println(k);
	}
	
	double getNewB(Double[] xi, double yi) {
		return b-eta*((yi*(dotProduct(w, xi) + b) < 1) ? C*(-yi) : 0);
	}
	
	public double[] getNewW(Double[] xi, double yi){
		double[] w2 = new double[w.length];
		ints(0, d).forEach(j->w2[j] = ((yi*(dotProduct(w, xi) + b)) < 1) ? w[j] - eta*(C*(-yi)*xi[j] + w[j]) : w[j] - eta*w[j]);
		return w2;
	}

	boolean isConverged() {
		System.out.println("cost: " + cost);
		return (cost = .5*(cost + (Math.abs(getCost(wOLD,bOLD) - getCost(w,b))*100)/getCost(wOLD,bOLD))) < epsilon ? true : false;
	}
	public double getCost(double[] wi, double bi){
		double sum1 = ints(0, d).mapToDouble(j->Math.pow(wi[j], 2)).sum();
		double sum2 = tL.stream().mapToDouble(t->Math.max(0,1-(t._2*(dotProduct(wi, t._1) + bi)))).sum();
		return .5*sum1 + C*sum2;
	}
}