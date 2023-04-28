package services
import com.google.inject.Inject
import models.{EmployeeForm,EmployeeList}
import scala.concurrent.Future
class EmployeeService @Inject() (employees:EmployeeList) {

  def addEmployee(employee:EmployeeForm):Future[String] = {
    employees.add(employee)
  }
  def deleteEmployee(companyID:String,employeeID:String):Future[String] = {
    employees.delete(companyID,employeeID)
  }
  def updateEmployee(employee: EmployeeForm):Future[String] = {
    employees.update(employee)
  }
  def getEmployee(companyID:String,employeeID:String):Future[Option[EmployeeForm]] = {
    employees.get(companyID,employeeID)
  }
  def getEmployees(companyID:String):Future[Seq[EmployeeForm]] = {
    employees.getByCompanyID(companyID)
  }
  def listAllEmployees:Future[Seq[EmployeeForm]] = {
    employees.listAll
  }

}