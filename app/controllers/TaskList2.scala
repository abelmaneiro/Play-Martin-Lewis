package controllers

import models.TaskListInMemoryModel
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton
class TaskList2 @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def load: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.version2Main())
  }

  def login: Action[AnyContent] = Action {
    Ok(views.html.login2())
  }

  def validate(username: String, password: String): Action[AnyContent] = Action {
    if (TaskListInMemoryModel.validateUser(username, password))
      Ok(views.html.taskList2(TaskListInMemoryModel.getTasks(username))).withSession("username" -> username)
    else
      Ok(views.html.login2())
  }

  def createUser(username: String, password: String): Action[AnyContent] = Action {
    if (TaskListInMemoryModel.createUser(username, password))
      Ok(views.html.taskList2(TaskListInMemoryModel.getTasks(username))).withSession("username" -> username)
    else
      Ok(views.html.login2())
  }

  def deleteTask(index: Int): Action[AnyContent] = Action { request =>
    request.session.get("username").map { username =>
      TaskListInMemoryModel.removeTask(username, index)
      Ok(views.html.taskList2(TaskListInMemoryModel.getTasks(username)))
    }.getOrElse(Ok(views.html.login2()))
  }

  def addTask(task: String): Action[AnyContent] = Action { request =>
    request.session.get("username").map { username =>
      TaskListInMemoryModel.addTask(username, task)
      Ok(views.html.taskList2(TaskListInMemoryModel.getTasks(username)))
    }.getOrElse(Ok(views.html.login2()))
  }

  def logout: Action[AnyContent] = Action { implicit request =>
    //Ok.apply(views.html.version2Main()).withNewSession  // This keeps the browser URL to /logout 2 so need to redirect
    //Redirect(views.html.version2Main()).withNewSession // can't redirect as need to be a URL not page
    Redirect(routes.TaskList2.load).withNewSession  // This changes the browser URL back to /load
  }

  def generatedJS = Action {
    Ok(views.js.generatedJS())
  }
}