import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;

public class Bd4Main extends LearnerHelp {
	
	void run() throws IOException {
		SparkSession ss = settings();
		LearnerData data = new LearnerData(ss, "bd4");
//		new Perceptron(data);
		new BatchGDSVM(data);
		new StochGDSVM(data);
		
//		ints(0, 1).forEach(x->new StochGDSVM(data));
		
//		Thread.sleep(20000);
		ss.close();
	}

	public static void main(String[] args) throws Exception {
		(new Bd4Main()).run();
	}
	
	/* Standard Setup */
	SparkSession settings() throws IOException {
		Logger.getLogger("org").setLevel(Level.WARN);
		Logger.getLogger("akka").setLevel(Level.WARN);
		SparkSession.clearActiveSession();
		ss = SparkSession.builder().appName("SVM").config("spark.master", "local").config("spark.eventlog.enabled","true").config("spark.executor.cores", "2").getOrCreate();
		SparkContext sc = ss.sparkContext();
		sc.setLogLevel("WARN");
		FileUtils.deleteDirectory(new File("output"));
		return ss;
	}
}