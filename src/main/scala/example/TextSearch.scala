package formation
import org.scalactic.TimesOnInt._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object TextSearch extends Greeting with App {
  def extractWords(line: String) : String = {
    return line
  }
  //val greeting = "hello world!"
  10 times println(greeting)
  val sparkContext = new SparkContext("local", "Scala Text Search")
  println(extractWords("aaaaa"))
}
trait Greeting {
  lazy val greeting = "hello world!"
}
