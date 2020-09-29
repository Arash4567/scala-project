package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import java.awt.Desktop.Action
import models.TaskListInMemoryModel

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents)
    extends BaseController {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index() =
    Action { implicit request: Request[AnyContent] =>
      Ok(views.html.index())
    }
  def ping() =
    Action { implicit request =>
      Ok("String work!")
    }
  def anotherOne =
    Action { _ =>
      Ok(Json.obj("yes" -> true))
    }
  def nameParam(name: String) =
    Action { _ =>
      Ok(Json.obj("name" -> name))
    }
  def posted =
    Action(parse.json) { implicit request =>
      Ok(Json.obj("recieved" -> Json.toJson(request.body)))
    }
  def taskList =
    Action {
      val username = "kali"
      val tasks = TaskListInMemoryModel.getTasks(username)
      Ok(views.html.taskList(tasks))
    }
  def product(prodType: String, prodNum: Int) =
    Action {
      Ok(s"Product type is: $prodType, product number is: $prodNum")
    }
    def login = Action {
      Ok(views.html.login1())
    }
    def validateLoginGet(username: String, password: String) = Action {
      Ok(s"$username logged in with $password")
    }
    def validateLoginPost =Action { request => 
      val postVals = request.body.asFormUrlEncoded
      postVals.map{ args =>
        val username = args("username").head
        val password = args("password").head
        if (TaskListInMemoryModel.validateUser(username, password)){
          Redirect(routes.HomeController.taskList())
        } else {
          Redirect(routes.HomeController.login())
        }
      }.getOrElse(Redirect(routes.HomeController.login()))
    }
}
