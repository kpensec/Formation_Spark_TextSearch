package formation
import org.scalactic.TimesOnInt._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
//import org.apache.spark.

object TextSearch extends Greeting with App {
  def extractWords(line: String) : String = {
    return line
  }

  
  for (e <- args) {
    println(e)
  }
  if (args.length <= 0) {
    sys.exit(-1)
  }
  val inputDirname = args(0);

  // 10 times println(greeting)
  val sparkContext = new SparkContext("local[1]", "Scala Text Search")
  val textFile = sparkContext.textFile(inputDirname)
  val rdd = textFile
    .flatMap(extractWords)
    .map(w => (w,1))
    .reduceByKey(_ + _)
  
      
  rdd.collect().foreach(println)

  println("Press return to quit")
  readLine();
  println("jobs done!")
  sparkContext.stop()
  
}
trait Greeting {
  lazy val greeting = "hello world!"
}
