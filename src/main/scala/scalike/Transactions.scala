package scalike

import scalikejdbc.{AutoSession, DB, DBSession, scalikejdbcSQLInterpolationImplicitDef}

object Transactions extends App {
  Initializer.init()

  def addCompany(name: String, address: Option[String])(implicit dbSession: DBSession = AutoSession): Int = {
    sql"INSERT INTO company(name, address) VALUES ($name, $address)".update().apply()
  }

  def getAllNames()(implicit dbSession: DBSession = AutoSession): List[Company] = {
    sql"select * from company".map(Company.from).list().apply()
  }

  // AutoSession provides a new session
  println(getAllNames())

  DB localTx { implicit session =>
    addCompany("RacComp", Some("Dust"))
    println(getAllNames())
  }

  println(getAllNames())
}
