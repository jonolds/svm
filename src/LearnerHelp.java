import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class LearnerHelp {
	SparkSession ss;
	JavaPairRDD<Long, Double[]> featsIDX;
	JavaPairRDD<Long, Double> labsIDX;
	JavaPairRDD<Double[], Double> train;
	JavaPairRDD<Long, List<Double>> featsIDXList;
	JavaPairRDD<List<Double>, Double> trainList;
	
	static <K, V1, V2> JavaPairRDD<V1, V2> valsToPairRDD(JavaPairRDD<K, Tuple2<V1, V2>> orig) {
		return orig.sortByKey().mapToPair(x->new Tuple2<>(x._2._1, x._2._2));
	}
	
	static <N extends Number>double dot(N[] w, N[] x) {
		return IntStream.range(0, w.length).mapToDouble(z->w[z].doubleValue()*x[z].doubleValue()).sum();
	}

	static double dot(Double[] w, Double[] x) {
		return IntStream.range(0, w.length).mapToDouble(z->w[z]*x[z]).sum();
	}
	
/* PARSE INTEGER[] FROM STRING JavaPairRDD */
	static List<Double> getDblListFromStr(String s) {
		return Arrays.stream(s.split(",")).map(z->Double.valueOf(z)).collect(Collectors.toList());
	}
	static Double[] getDblArrFromStr(String s) {
		return Arrays.stream(s.split(",")).map(z->Double.valueOf(z)).toArray(Double[]::new);
	}

/* PRINT ARRAY */
	static <T>String toStr(T[] arr) {
		return Arrays.stream(arr).map(x->x.toString()).collect(Collectors.joining(", "));
	}
	
	static String toStr(double[] arr) {
		return Arrays.stream(arr).mapToObj(x->((Double)x).toString()).collect(Collectors.joining(", "));
	}
	
/* SWAP JavaPairRDD */
	static <K, V> JavaPairRDD<V, K> swap(JavaPairRDD<K, V> orig) {
		return orig.mapToPair(x->new Tuple2<>(x._2, x._1));
	}
	
//	static <N extends Number> double sum(N...t) {
//		return Arrays.stream(t).mapToDouble(x->x.doubleValue()).sum();
//	}
	
/* Copy ArrayList (DEEP) */
	static <T>ArrayList<List<T>> copy(ArrayList<List<T>> cent) {
		return (ArrayList<List<T>>)cent.stream().map(x->(x.stream().collect(Collectors.toList()))).collect(Collectors.toList());
	}
	
	static Double[] copy(Double[] orig) {
		return Arrays.stream(orig).mapToDouble(x->x.doubleValue()).boxed().toArray(Double[]::new);
	}
	
	static <T>void print(T s) { System.out.print(s); }
	static <T>void println(T s) { System.out.println(s); }
}



//double dot(double[] w, Integer[] x) {
//return IntStream.range(0, w.length).mapToDouble(z->w[z]*x[z]).sum();
//}
//double[] adjustW(double[] oldW, float sign, Integer[] x) {
//return IntStream.range(0, oldW.length).mapToDouble(z->oldW[z] + (x[z]*sign*n)).toArray();
//}