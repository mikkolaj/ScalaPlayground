package database_access

import scalikejdbc.config._
import scalikejdbc.{scalikejdbcSQLInterpolationImplicitDef, DB}

object Initializer {
  def init(): Unit = {
    DBs.setupAll()
    bootstrapDb()
  }

  def stop(): Unit = {
    DBs.closeAll()
  }

  private def bootstrapDb(): Unit = {
    DB.localTx { implicit session =>
      sql"""CREATE TABLE IF NOT EXISTS Company(
        ID SERIAL PRIMARY KEY NOT NULL,
        NAME TEXT NOT NULL,
        ADDRESS TEXT
      )""".executeUpdate().apply()
    }

    val companyName = "TastyCompany"
    val companyAddress = "Office"

    DB.localTx { implicit session =>
      sql"""INSERT INTO Company(ID, NAME, ADDRESS)
         VALUES (1, $companyName, $companyAddress)
         ON CONFLICT (ID)
         DO UPDATE SET NAME = $companyName, ADDRESS = $companyAddress
         """.executeUpdate().apply()
    }
  }
}
