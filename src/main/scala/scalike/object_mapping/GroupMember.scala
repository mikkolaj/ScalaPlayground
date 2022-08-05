package scalike.object_mapping

import scalikejdbc._

case class GroupMember(id: Long, name: String, groupId: Option[Long] = None, group: Option[Group] = None)

object GroupMember extends SQLSyntaxSupport[GroupMember] {
  def apply(m: ResultName[GroupMember])(rs: WrappedResultSet) =
    new GroupMember(rs.long(m.id), rs.string(m.name), rs.longOpt(m.groupId))

  def apply(m: ResultName[GroupMember], g: ResultName[Group])(rs: WrappedResultSet) = {
    apply(m)(rs).copy(group = rs.longOpt(g.id).map(_ => Group(g)(rs)))
  }
}
