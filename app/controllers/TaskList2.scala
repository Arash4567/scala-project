package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.TaskListInMemoryModel

@Singleton
class TaskList2 @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
    def load = Action { implicit request =>
        Ok(views.html.version2Main())
    }
    def login = Action{
        Ok(views.html.login2())
    }
}