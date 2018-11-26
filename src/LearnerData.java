import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class LearnerData extends LearnerHelp {
	SparkSession ss;
	
	List<Double> lL;
	List<Double[]> fL;
	List<Tuple2<Double[],Double>> tL;
	
	JavaPairRDD<Integer, Double[]> featsIDX;
	JavaPairRDD<Integer, Double> labsIDX;
	
	LearnerData(SparkSession ss, String set) {
		this.ss = ss;
		
		featsIDX = getIdxLine(set + "_features.txt").mapToPair(x->new Tuple2<>(Math.toIntExact(x._1), strToDblArr(x._2)));
		fL = featsIDX.map(x->x._2).collect();
		
		labsIDX = getIdxLine(set + "_labels.txt").mapToPair(x->new Tuple2<>(Math.toIntExact(x._1), Double.valueOf(x._2)));
		lL = labsIDX.map(x->x._2).collect();
		
		tL = featsIDX.join(labsIDX).sortByKey().mapToPair(x->new Tuple2<>(x._2._1, x._2._2)).collect();		
	}
	
	JavaPairRDD<Long, String> getIdxLine(String name) {
		return swap(ss.read().textFile(name).javaRDD().zipWithIndex());
	}
	
}