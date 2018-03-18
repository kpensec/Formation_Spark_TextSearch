package formation

import org.apache.spark.sql.SparkSession
import scala.io.StdIn.readLine

object TextSearch extends App {

  def stripAll(bad: String) = {
    (word : String) => {
    import scala.annotation.tailrec
      @tailrec def start(n: Int): String = 
        if (n == word.length) ""
        else if (bad.indexOf(word.charAt(n)) < 0) end(n, word.length)
        else start(1 + n)

      @tailrec def end(a: Int, n: Int): String =
        if (n <= a) word.substring(a, n)
        else if (bad.indexOf(word.charAt(n - 1)) < 0) word.substring(a, n)
        else end(a, n - 1)

      start(0)
    }
  }

  def extractWords(line: String) : Array[String] = line
    .split(" ")
    .map(stripAll("!?,.:;@#()[]{}$*'/\\-|"))
    .filter(_!="")

  case class WordCountData (
    word: String,
    count: BigInt,
    length: Int
  )

  val QUERY_MOST_FREQUENT_NSIZED_WORD =
  """ SELECT *
    | FROM WordCountData
    | WHERE length = %d
    | ORDER BY count DESC
    | LIMIT 10
  """.stripMargin
  
  val QUERY_LONGEST_MOST_FREQUENT_WORD =
  """ SELECT *
    | FROM WordCountData
    | ORDER BY length DESC, count DESC
    | LIMIT 10
  """.stripMargin

  def prepareForDataSet(kv: (String, BigInt)) = WordCountData(kv._1, kv._2, kv._1.length())
  
  def usage() {
    println("run <inputURI>")
  }

  for (e <- args) {
    println(e)
  }
  if (args.length <= 0) {
    usage()
    sys.exit(-1)
  }
  val inputDirname = args(0);
  
  val sparkSession = SparkSession.builder
    .appName("Scala/Spark Text Search")
    .master("local[1]")
    .getOrCreate()

  try {
    import sparkSession.implicits._
    sparkSession.sparkContext.setLogLevel("ERROR")
    val lines = sparkSession.sqlContext.read.text(inputDirname).as[String]
    val words = lines.flatMap(extractWords)
    val wordCount = words.groupBy("value").count().as[(String, BigInt)]
    val ds = wordCount.map(prepareForDataSet)
    ds.cache().createTempView("WordCountData")

    sparkSession.sql(QUERY_MOST_FREQUENT_NSIZED_WORD.format(4)).show()
    sparkSession.sql(QUERY_MOST_FREQUENT_NSIZED_WORD.format(15)).show()
    sparkSession.sql(QUERY_LONGEST_MOST_FREQUENT_WORD).show()
  } catch {
    case e: Exception => {
      println("exception caught " + e )
      e.printStackTrace()
    }
  } finally {
    sparkSession.close()
  }
  readLine("Press Return to quit...");
}
