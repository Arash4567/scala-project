package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.TaskListInMemoryModel
import play.api.data._
import play.api.data.Forms._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
case class LoginData(username: String, password: String)
@Singleton
class HomeController @Inject() (cc: MessagesControllerComponents)
    extends MessagesAbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  val loginForm = Form(
    mapping(
      "Username" -> text(3, 10),
      "Password" -> text(4)
    )(LoginData.apply)(LoginData.unapply)
  )

  def index() =
    Action { implicit request =>
      Ok(views.html.index("SharedMessage.itWorks"))
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

  def product(prodType: String, prodNum: Int) =
    Action {
      Ok(s"Product type is: $prodType, product number is: $prodNum")
    }
  def login =
    Action { implicit request =>
      Ok(views.html.login1(loginForm))
    }
  def validateLoginGet(username: String, password: String) =
    Action {
      Ok(s"$username logged in with $password")
    }
  def validateLoginPost =
    Action { implicit request =>
      val postVals = request.body.asFormUrlEncoded
      postVals
        .map { args =>
          val username = args("username").head
          val password = args("password").head
          if (TaskListInMemoryModel.validateUser(username, password)) {
            Redirect(routes.HomeController.taskList())
              .withSession("username" -> username)
          } else {
            Redirect(routes.HomeController.login())
              .flashing("error" -> "Invalid username or password!")
          }
        }
        .getOrElse(Redirect(routes.HomeController.login()))
    }

  def validateLoginForm =
    Action { implicit request =>
      loginForm.bindFromRequest.fold(
        forWithError => BadRequest(views.html.login1(forWithError)),
        ld =>
          if (TaskListInMemoryModel.validateUser(ld.username, ld.password)) {
            Redirect(routes.HomeController.taskList())
              .withSession("username" -> ld.username)
          } else {
            Redirect(routes.HomeController.login())
              .flashing("error" -> "Invalid username or password!")
          }
      )
    }

  def createUser =
    Action { implicit request =>
      val postVals = request.body.asFormUrlEncoded
      postVals
        .map { args =>
          val username = args("username").head
          val password = args("password").head
          if (TaskListInMemoryModel.createUser(username, password)) {
            Redirect(routes.HomeController.taskList())
              .withSession("username" -> username)
          } else {
            Redirect(routes.HomeController.login())
              .flashing("error" -> "User creation failed!")
          }
        }
        .getOrElse(Redirect(routes.HomeController.login()))
    }

  def taskList =
    Action { implicit request =>
      val usernameOption = request.session.get("username")
      usernameOption
        .map { username =>
          val tasks = TaskListInMemoryModel.getTasks(username)
          Ok(views.html.taskList(tasks))
        }
        .getOrElse(Redirect(routes.HomeController.login()))
    }

  def logout =
    Action {
      Redirect(routes.HomeController.login()).withSession()
    }

  def addTask =
    Action { implicit request =>
      val usernameOption = request.session.get("username")
      usernameOption
        .map { username =>
          val postVals = request.body.asFormUrlEncoded
          postVals
            .map { args =>
              val task = args("newTask").head
              TaskListInMemoryModel.addTask(username, task)
              Redirect(routes.HomeController.taskList())
            }
            .getOrElse(Redirect(routes.HomeController.taskList()))
        }
        .getOrElse(Redirect(routes.HomeController.login()))
    }

  def deleteTask =
    Action { implicit request =>
      val usernameOption = request.session.get("username")
      usernameOption
        .map { username =>
          val postVals = request.body.asFormUrlEncoded
          postVals
            .map { args =>
              val index = args("index").head.toInt
              TaskListInMemoryModel.removeTask(username, index)
              Redirect(routes.HomeController.taskList())
            }
            .getOrElse(Redirect(routes.HomeController.taskList()))
        }
        .getOrElse(Redirect(routes.HomeController.login()))
    }
}
