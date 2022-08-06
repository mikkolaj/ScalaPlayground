package database_access.orm

import scalikejdbc._
import skinny.orm._

import java.time.LocalDateTime

case class Member(id: Long, name: Option[String], createdAt: LocalDateTime)

object Member extends SkinnyMapper[Member] {
  override lazy val defaultAlias = createAlias("m")

  override def extract(rs: WrappedResultSet, n: ResultName[Member]): Member = {
    new Member(id = rs.get(n.id), name = rs.get(n.name), createdAt = rs.get(n.createdAt))
  }
}
