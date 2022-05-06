def foo: () => Int = () => return () => 1
val x = foo

foo()


def phoo: Int = {
  val sumR: List[Int] => Int = _.foldLeft(0)((n, m) => return n + m)
  sumR(List(1,2,3)) + sumR(List(4,5,6))
}

phoo

// if
def foo2(n:Int): Int = {
  if (n < 100) n else return 100
}

// then why not
def foo3(n: Int): Int = {
  val a = return 100
  if (n < 100) n else a
}

foo2(1)

def max200(ns: List[Int]): Int =
  ns.foldLeft(0) { (n, m) =>
    if (n + m > 200)
      return 200
    else
      n + m
  }


max200((1 to 100).toList)