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
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class SVMHelp {
	SparkSession ss;

/* PARSE INTEGER[] FROM STRING JavaPairRDD */
	static List<Integer> getIntsFromStr(String s) {
		return Arrays.stream(s.split(",")).map(z->Integer.valueOf(z)).collect(Collectors.toList());
	}
	
/* READ WITH INDEX JavaPairRDD */
	JavaPairRDD<String, Long> readStrIdx(String name) {
		return ss.read().textFile(name).javaRDD().zipWithIndex();
	}
	JavaPairRDD<Long, String> readIdxStr(String name) {
		return swap(ss.read().textFile(name).javaRDD().zipWithIndex());
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
