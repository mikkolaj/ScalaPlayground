package database_access

import scalikejdbc._

object ReadTest extends App {
  Initializer.init()

  // using DBSession directly
  val result: Option[Company] = DB.readOnly { session: DBSession =>
    // using SQL Interpolation
    session.single("select * from Company where id = ?", 1) { resultSet =>
      Company.from(resultSet)
    }
  }

  // using implicit DBSession
  val resultImplicit: Option[Company] = DB.readOnly { implicit session: DBSession =>
    sql"select * from Company where id = 1"
      .map(resultSet => Company.from(resultSet))
      .single()
      .apply()
  }

  println(result)
  println(resultImplicit)
}
