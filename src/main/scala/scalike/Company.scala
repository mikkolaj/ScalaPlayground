package scalike

import scalikejdbc.WrappedResultSet

case class Company(id: Int, name: String, address: String)

object Company {
  def from(resultSet: WrappedResultSet): Company = {
    // values can be extracted based on both column names and column indices
    //    new CompanyRow(resultSet.int(1), resultSet.string(2), resultSet.string(3))
    new Company(resultSet.int("id"), resultSet.string("name"), resultSet.string("address"))
  }
}
