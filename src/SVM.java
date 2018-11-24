import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

import org.apache.spark.api.java.function.*;

@SuppressWarnings("unused")
public class SVM extends SVMHelp {
	
	void GD() throws Exception {
		JavaPairRDD<Long, List<Integer>> feats = readIdxStr("features.train.txt").mapToPair(x->new Tuple2<>(x._1, getIntsFromStr(x._2)));
		JavaPairRDD<Long, Integer> labs = readIdxStr("target.train.txt").mapToPair(x->new Tuple2<>(x._1, Integer.valueOf(x._2)));

		JavaPairRDD<List<Integer>, Integer> train = feats.join(labs).sortByKey().mapToPair(x->new Tuple2<>(x._2._1, x._2._2));

		double[] w = new double[feats.first()._2.size()];
		System.out.println(w.length);
	}
	
//	class FlatFunc implements FlatMapFunction<String[], Integer[]> {
//		public Iterator<Integer[]> call(String[] t) throws Exception {
//			List<Integer> list = Arrays.stream(t).mapToInt(x->Integer.parseInt(x)).boxed().collect(Collectors.toList());
//			List<Integer[]> l2 = IntStream.range(0, t.length).map(mapper)
//			return null;
//		}
//	}
	
	
	


	SVM() throws Exception {
		settings();
		GD();
//		Thread.sleep(20000);
		ss.close();
	}

/* MAIN */
	public static void main(String[] args) throws Exception {
		SVM svm = new SVM();
	}
}