package database_access.orm

import database_access.Initializer
import scalikejdbc._

object SkinnyMapperExample extends App {
  Initializer.init()
  implicit val session: AutoSession = AutoSession

  sql"create table member (id serial, name varchar(64), created_at timestamp)".execute.apply()
  sql"insert into member(id, name, created_at) values(123, 'Tasty', now())".execute.apply()

  // basic trait, implements Querying API and Finder API
  val member: Option[Member] = Member.findById(123)
  val members: Seq[Member] = Member.where(Symbol("name") -> "Tasty").apply()

  println(member)
  println(members)
}
