val statefulLambda = () => {
  var power = 1L

  (element: Int) => {
    power *= 2
    List(element, power).map(_.toString)
  }
}

val producer = statefulLambda()

println(producer(1))
println(producer(2))
println(producer(4))
