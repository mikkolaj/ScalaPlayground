package akka_streams.datastructures

case class LineRecord(key: Int, value: Int)

object LineRecord {
  def fromLine(line: String): LineRecord = {
    val split = line.split(",")
    LineRecord(split(0).toInt, split(1).toInt)
  }
}
