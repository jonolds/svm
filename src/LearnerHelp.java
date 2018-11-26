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
	
	static double dotProduct(double[] w, Double[] x) {
		return ints(0, w.length).mapToDouble(z->w[z]*x[z]).sum();
	}
	static double dotProduct(double[] w, double[] x) {
		return ints(0, w.length).mapToDouble(z->w[z]*x[z]).sum();
	}
	
	static IntStream ints(int from, int until) {
		return IntStream.range(from, until);
	}
	
/* PARSE INTEGER[] FROM STRING JavaPairRDD */
	static Double[] strToDblArr(String s) {
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

/* Copy ArrayList (DEEP) */
	static <T>ArrayList<List<T>> copy(ArrayList<List<T>> cent) {
		return (ArrayList<List<T>>)cent.stream().map(x->(x.stream().collect(Collectors.toList()))).collect(Collectors.toList());
	}
	
	static double[] copy(double[] orig) {
		return Arrays.stream(orig).map(x->x).toArray();
	}
	
	static <T>void print(T s) { System.out.print(s); }
	static <T>void println(T s) { System.out.println(s); }
}