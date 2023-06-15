package memory_testing

import java.io._
import scala.io.Source

object FileReadingTest extends App {
  val file = new File("test.txt")

  // write content
//  val writer = new FileWriter(file)
//  val line: String = ('a' to 'z').mkString.concat("\n")
//
//  (0 to 1000000).foreach(_ => writer.write(line))
//
//  writer.close()

  val fileInputStream: InputStream = new FileInputStream(file)

  val start: Long = System.currentTimeMillis();

  // Option 1: BR
  val reader = new BufferedReader(new InputStreamReader(fileInputStream))
  LazyList.continually(reader.readLine()).takeWhile(_ != null).mkString
  reader.close()

  // Option 2: Source
//  val reader = Source.fromInputStream(fileInputStream)
//  reader.getLines().mkString
//  reader.close()

  val end: Long = System.currentTimeMillis();

  println(s"Computation time: ${end - start}ms")
}
