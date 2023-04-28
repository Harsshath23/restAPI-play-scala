package controllers.api
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{EmployeeForm,EmployeeFormData}
import play.api.data.FormError

import services.EmployeeService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EmployeeController @Inject()(cc:ControllerComponents,employeeService: EmployeeService
                                  ) extends AbstractController(cc){
  implicit  val employeeFormat = Json.format[EmployeeForm]

  def getAll = Action.async { implicit request:Request[AnyContent] =>
    employeeService.listAllEmployees map { employees =>
      Ok(Json.toJson(employees))
    }
  }
  def getByID(companyID:String,employeeID:String) = Action.async {implicit request:Request[AnyContent] =>
    employeeService.getEmployee(companyID, employeeID) map { employee =>
      Ok(Json.toJson(employee))
    }
  }
  def getByCompanyID(companyID:String) = Action.async {implicit request:Request[AnyContent] =>
    employeeService.getEmployees(companyID)  map { employee =>
      Ok(Json.toJson(employee))
    }
  }
  def add() = Action.async { implicit request: Request[AnyContent] =>
    EmployeeFormData.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val newEmployee = EmployeeForm(data.id,data.employeeID,data.companyID,data.name,data.position,data.address)
        employeeService.addEmployee(newEmployee).map(_ => Redirect(routes.EmployeeController.getAll))
      }
    )
  }
  def update(employeeID:String) =Action.async { implicit request: Request[AnyContent] =>
    EmployeeFormData.form.bindFromRequest.fold(
      errorForm => {
        errorForm.errors.foreach(println)
        Future.successful(BadRequest("Error!"))
      },
      data => {
        val employee = EmployeeForm(data.id,employeeID,data.companyID,data.name ,data.position ,data.address)
        employeeService.updateEmployee(employee).map( _ => Redirect(routes.EmployeeController.getAll))
      }
    )
  }
  def delete(companyID:String,employeeID:String)  = Action.async{ implicit request: Request[AnyContent] =>
    employeeService.deleteEmployee(companyID, employeeID) map { res =>
      Redirect(routes.EmployeeController.getAll)
    }
  }



}