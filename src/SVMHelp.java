import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class SVMHelp {
	SparkSession ss;
	JavaPairRDD<Long, Integer[]> featsIDX;
	JavaPairRDD<Long, Integer> labsIDX;
	JavaPairRDD<Integer[], Integer> train;
	JavaPairRDD<Long, List<Integer>> featsIDXList;
	JavaPairRDD<List<Integer>, Integer> trainList;
	
	static <K, V1, V2> JavaPairRDD<V1, V2> valsToPairRDD(JavaPairRDD<K, Tuple2<V1, V2>> orig) {
		return orig.sortByKey().mapToPair(x->new Tuple2<>(x._2._1, x._2._2));
	}

/* PARSE INTEGER[] FROM STRING JavaPairRDD */
	static List<Integer> getIntListFromStr(String s) {
		return Arrays.stream(s.split(",")).map(z->Integer.valueOf(z)).collect(Collectors.toList());
	}
	static Integer[] getIntArrFromStr(String s) {
		return Arrays.stream(s.split(",")).map(z->Integer.valueOf(z)).toArray(Integer[]::new);
	}
	
/* READ WITH INDEX JavaPairRDD */
	JavaPairRDD<Long, String> getIdxLine(String name) {
		return swap(ss.read().textFile(name).javaRDD().zipWithIndex());
	}

	JavaRDD<Integer> getLabs(String name) {
		return getLabsIDX(name).map(x->x._2);
	}
	JavaPairRDD<Long, Integer> getLabsIDX(String name) {
		return labsIDX = getIdxLine(name).mapToPair(x->new Tuple2<>(x._1, Integer.valueOf(x._2)));
	}
	
	JavaRDD<Integer[]> getFeats(String name) {
		return getFeatsIDX(name).map(x->x._2);
	}
	JavaPairRDD<Long, Integer[]> getFeatsIDX(String name) {
		return featsIDX = getIdxLine(name).mapToPair(x->new Tuple2<>(x._1, getIntArrFromStr(x._2)));
	}
	
	JavaRDD<List<Integer>> getFeatsList(String name) {
		return getFeatsIDXList(name).map(x->x._2);
	}
	JavaPairRDD<Long, List<Integer>> getFeatsIDXList(String name) {
		return featsIDXList = getIdxLine(name).mapToPair(x->new Tuple2<>(x._1, getIntListFromStr(x._2)));
	}


/* PRINT ARRAY */
	static <T> void print(T[] t) {
		if(t.length > 0) {
			System.out.print(t[0]);
			for(int i = 1; i < t.length; i++)
				System.out.print(", " + t[i]);
			System.out.println();
		}
	}
	static void print(double[] t) {
		if(t.length > 0) {
			System.out.print(t[0]);
			for(int i = 1; i < t.length; i++)
				System.out.print(", " + t[i]);
			System.out.println();
		}
	}
	static String toStr(Integer[] arr) {
		String s = "";
		if(arr.length > 0) {
			s+=(arr[0]);
			for(int i = 1; i < arr.length; i++)
				s += (", " + arr[i]);
		}
		return s;
	}
	
	static String toStr(double[] arr) {
		String s = "";
		if(arr.length > 0) {
			s+=(arr[0]);
			for(int i = 1; i < arr.length; i++)
				s += (", " + arr[i]);
		}
		return s;
	}
	
	
/* SWAP JavaPairRDD */
	static <K, V> JavaPairRDD<V, K> swap(JavaPairRDD<K, V> orig) {
		return orig.mapToPair(x->new Tuple2<>(x._2, x._1));
	}
	
/* Copy ArrayList (DEEP) */
	static <T>ArrayList<List<T>> copyArrList(ArrayList<List<T>> cent) {
		return (ArrayList<List<T>>)cent.stream().map(x->(x.stream().collect(Collectors.toList()))).collect(Collectors.toList());
	}

/* Standard Setup */
	void settings() throws IOException {
		Logger.getLogger("org").setLevel(Level.WARN);
		Logger.getLogger("akka").setLevel(Level.WARN);
		SparkSession.clearActiveSession();
		ss = SparkSession.builder().appName("SVM").config("spark.master", "local").config("spark.eventlog.enabled","true").config("spark.executor.cores", "2").getOrCreate();
		SparkContext sc = ss.sparkContext();
		sc.setLogLevel("WARN");
		FileUtils.deleteDirectory(new File("output"));
	}
}
