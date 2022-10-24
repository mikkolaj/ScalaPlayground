package database_access.object_mapping

import database_access.Initializer
import scalikejdbc._

object InterpolationOfMappings extends App {
  Initializer.init()

  val id = 123

  val (m, g) = (GroupMember.syntax("m"), Group.syntax("g"))
  DB.localTx { implicit dbSession =>
    val groupMember: Option[GroupMember] = sql"""
  select
    ${m.result.*}, ${g.result.*}
  from
    ${GroupMember.as(m)} left join ${Group.as(g)} on ${m.groupId} = ${g.id}
  where
    ${m.id} = ${id}"""
      .map(GroupMember(m.resultName, g.resultName))
      .single
      .apply()
    println(groupMember)
  }

}
