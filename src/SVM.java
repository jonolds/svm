import java.util.List;
import java.util.stream.IntStream;

import org.apache.spark.api.java.JavaRDD;

@SuppressWarnings("unused")
public class SVM extends SVMHelp {
	JavaRDD<Integer[]> feats;
	JavaRDD<Integer> labs;
	
	static final int C = 100;
	static final double n = .5;
	
	void batchSG() {
		double[] w = new double[feats.first().length];
		int correct = 0, cur_ex = 0;
		List<Integer[]> examples = feats.collect();
		List<Integer> lab = labs.collect();
		
		while(correct < examples.size()) {
			Integer[] ex = examples.get(cur_ex);
			double wdotx = wDotX(w, examples.get(cur_ex));
			double y = lab.get(cur_ex);
			
			System.out.println("cur_ex: " + cur_ex + "\nex: " + toStr(ex));
			System.out.println("w: " + toStr(w) + "\nwdotx: " + wdotx);
			System.out.println("Math.signum(wdotx): " + Math.signum(wdotx) + "\ny: " + y);
			
			if(Math.signum(wdotx) == y) {
				correct++;
				System.out.println("Hit. Correct in a row: " + correct);
			}
			else {
				correct = 0;
				w = adjustW(w, Math.signum(lab.get(cur_ex)), examples.get(cur_ex));
				System.out.println("Miss. ** w = [" + toStr(w) + "]");
			}
			cur_ex += (cur_ex == (examples.size()-1)) ? (-cur_ex) : 1;
			System.out.println("Correct: " + correct + "\n");
		}
		System.out.println("final w: " + toStr(w));
	}
	
	double wDotX(double[] w, Integer[] x) {
		return IntStream.range(0, w.length).mapToDouble(z->w[z]*x[z]).sum();
	}
	
	double[] adjustW(double[] oldW, float sign, Integer[] x) {
		double[] newW = new double[oldW.length];
		for(int i = 0; i < oldW.length; i++)
			newW[i] = oldW[i] + (x[i]*sign*n);
		return newW;
	}
	

	
//	feats.saveAsTextFile("output/out1");
//	labs.saveAsTextFile("output/out2");
//	train.saveAsTextFile("output/out3");
//	featsIDX.saveAsTextFile("output/out4");
//	labsIDX.saveAsTextFile("output/out5");
	
	SVM() throws Exception {
		settings();
		loadData();
		batchSG();
//		perceptron();
//		Thread.sleep(20000);
		ss.close();
	}

	void loadData() {
		feats = getFeats("feats2.txt");
		labs = getLabs("labs2.txt");
		train = valsToPairRDD(featsIDX.join(labsIDX));
	}

/* MAIN */
	public static void main(String[] args) throws Exception {
		SVM svm = new SVM();
	}
	void stochGD() {
	}
	
	
	void perceptron() {
		double[] w = new double[feats.first().length];
		int correct = 0, cur_ex = 0;
		List<Integer[]> examples = feats.collect();
		List<Integer> lab = labs.collect();
		
		while(correct < examples.size()) {
			Integer[] ex = examples.get(cur_ex);
			double wdotx = wDotX(w, examples.get(cur_ex));
			double y = lab.get(cur_ex);
			
			System.out.println("cur_ex: " + cur_ex + "\nex: " + toStr(ex));
			System.out.println("w: " + toStr(w) + "\nwdotx: " + wdotx);
			System.out.println("Math.signum(wdotx): " + Math.signum(wdotx) + "\ny: " + y);
			
			if(Math.signum(wdotx) == y) {
				correct++;
				System.out.println("Hit. Correct in a row: " + correct);
			}
			else {
				correct = 0;
				w = adjustW(w, Math.signum(lab.get(cur_ex)), examples.get(cur_ex));
				System.out.println("Miss. ** w = [" + toStr(w) + "]");
			}
			cur_ex += (cur_ex == (examples.size()-1)) ? (-cur_ex) : 1;
			System.out.println("Correct: " + correct + "\n");
		}
		System.out.println("final w: " + toStr(w));
	}
}