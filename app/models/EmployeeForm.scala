package models
import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._

case class EmployeeForm(
                         id:String,
                         employeeID : String,
                         companyID: String,
                         name: String,
                         position: String,
                         address: String
                       )
case class EmployeeFormData( id:String,
                             employeeID:String,
                            companyID: String,
                            name: String,
                            position: String,
                            address: String
                           )

object EmployeeFormData {
  val form = Form(
    mapping(
      "id"->nonEmptyText,
      "employeeID"->nonEmptyText,
      "companyID"->nonEmptyText,
      //"employeeID"-> number,
      "name"-> nonEmptyText,
      "position"->nonEmptyText,
      "address"->nonEmptyText
    )(EmployeeFormData.apply)(EmployeeFormData.unapply)
  )
}

class EmployeeTable(tag: Tag) extends  Table[EmployeeForm](tag,"EmployeeForm") {
  def id =column[String]("id",O.PrimaryKey)
  def employeeID = column[String]("employeeID")
  def companyID= column[String]("companyID")
  //def employeeID = column[Int]("employeeID")
  def name = column[String]("name")
  def position = column[String]("position")
  def address = column[String]("address")

  override def * = (id,employeeID,companyID,name,position,address) <> (EmployeeForm.tupled, EmployeeForm.unapply)
}

class EmployeeList @Inject()(
                              protected val dbConfigProvider: DatabaseConfigProvider
                            )(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  var employeeList = TableQuery[EmployeeTable]
  def add(employee:EmployeeForm):Future[String] = {
    dbConfig.db.run(employeeList +=employee).map(res => "Employee successfully added").recover {
      case ex: Exception => {
        printf(ex.getMessage())
        ex.getMessage
      }
    }
  }
  def delete(companyID:String,employeeID:String):Future[String] = {
    dbConfig.db.run(employeeList.filter(_.companyID===companyID).filter(_.employeeID === employeeID).delete).map { numDeleted =>
      if(numDeleted > 0) {
        "Employee successfully deleted"
      } else {
        s"No employee found with ID $employeeID"
      }
    }
  }
  def update(employee:EmployeeForm):Future[String] = {
    val query = employeeList.filter(_.companyID===employee.companyID).filter(_.employeeID===employee.employeeID)
      .map(x => (x.id,x.name,x.employeeID,x.companyID,x.address,x.position))
      .update(employee.id,employee.name,employee.employeeID,employee.companyID,employee.address,employee.position)
    dbConfig.db.run(query).map { numUpdated =>
      if(numUpdated > 0) {
        "Employee successfully updated"
      } else {
        s"No employee found with ID ${employee.employeeID}"
      }
    }
  }
  def get(companyID:String,employeeID:String):Future[Option[EmployeeForm]] = {
    dbConfig.db.run(employeeList.filter(_.companyID===companyID).filter(_.employeeID === employeeID).result.headOption)
  }

  def getByCompanyID(companyID: String): Future[Seq[EmployeeForm]] = {
    dbConfig.db.run(employeeList.filter(_.companyID === companyID).result)
  }

  def listAll: Future[Seq[EmployeeForm]] = {
    dbConfig.db.run(employeeList.result)
  }
}