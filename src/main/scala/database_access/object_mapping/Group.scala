package database_access.object_mapping

import scalikejdbc._

case class Group(id: Long, name: String)

object Group extends SQLSyntaxSupport[Group] {

  // If you need to specify schema name, override this
  // def table will return sqls"public.groups" in this case
  // Of course, schemaName doesn't work with MySQL
  override val schemaName = Some("public")

  // If the table name is same as snake_case'd name of this companion object,
  // you don't need to specify tableName explicitly.
  override val tableName = "groups"

  // If you use NamedDB for this entity, override connectionPoolName
  //override val connectionPoolName = 'anotherdb

  def apply(g: ResultName[Group])(rs: WrappedResultSet) =
    new Group(rs.long(g.id), rs.string(g.name))
}
