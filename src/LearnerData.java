import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class LearnerData extends LearnerHelp {
	SparkSession ss;
	
	JavaRDD<Double[]> feats;
	JavaRDD<Double> labs;
	JavaPairRDD<Double[], Double> train;
	
	JavaPairRDD<Long, Double[]> featsIDX;
	JavaPairRDD<Long, Double> labsIDX;
	JavaPairRDD<Long, List<Double>> featsIDXList;
	JavaPairRDD<List<Double>, Double> trainList;
	
	LearnerData(SparkSession ss, String set) {
		this.ss = ss;
		feats = getFeats(set + "_features.txt");
		labs = getLabs(set + "_labels.txt");
		train = valsToPairRDD(featsIDX.join(labsIDX));
		
	}
	
	/* READ WITH INDEX JavaPairRDD */
	JavaPairRDD<Long, String> getIdxLine(String name) {
		return swap(ss.read().textFile(name).javaRDD().zipWithIndex());
	}

	JavaRDD<Double> getLabs(String name) {
		return getLabsIDX(name).map(x->x._2);
	}
	JavaPairRDD<Long, Double> getLabsIDX(String name) {
		return labsIDX = getIdxLine(name).mapToPair(x->new Tuple2<>(x._1, Double.valueOf(x._2)));
	}
	
	JavaRDD<Double[]> getFeats(String name) {
		return getFeatsIDX(name).map(x->x._2);
	}
	JavaPairRDD<Long, Double[]> getFeatsIDX(String name) {
		return featsIDX = getIdxLine(name).mapToPair(x->new Tuple2<>(x._1, getDblArrFromStr(x._2)));
	}
	
	JavaRDD<List<Double>> getFeatsList(String name) {
		return getFeatsIDXList(name).map(x->x._2);
	}
	JavaPairRDD<Long, List<Double>> getFeatsIDXList(String name) {
		return featsIDXList = getIdxLine(name).mapToPair(x->new Tuple2<>(x._1, getDblListFromStr(x._2)));
	}
}
