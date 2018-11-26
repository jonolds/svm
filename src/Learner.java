import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import scala.Tuple2;

public abstract class Learner extends LearnerHelp {
	JavaPairRDD<Double[], Double> train;
	JavaRDD<Double[]> feats;
	JavaRDD<Double> labs;
	
	static int n, d, C;
	static double eta, epsilon, b;
	Double[] w;
	
	Learner(LearnerData data) {
		this.train = data.train.mapToPair(x->new Tuple2<>(x._1, x._2));
		this.feats = data.feats;
		this.labs = data.labs;
		n = Math.toIntExact(train.count());
	}
}
