package controllers

import models.TaskListInMemoryModel
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton  // Juice injection which Play used by default for dependency injection
class TaskList1 @Inject()(cc: ControllerComponents) extends AbstractController(cc) { // need to inject the
                                                            // Controller Components and extend the Controller class
  // def login1: Action[AnyContent] = Action {request =>
  //  Ok(views.html.login1()(request))
  // }
  def login1: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.login1())  // By making request implicit, no need to pass it in
  }

  def validateLogin1Get(username: String, password: String): Action[AnyContent] = Action {
    Ok(s"$username login with $password")
  }

  def validateLogin1Post: Action[AnyContent] = Action { request =>
    val body = request.body.asFormUrlEncoded  //  is type of Option[Map[String, Seq[String]]]
    val credentials = body.map { args =>   // is type of Option[Result]
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.validateUser(username, password))
        Redirect(routes.TaskList1.taskList).withSession("username" -> username) // redirect to taskList page using reverse routing
      else
        Redirect(routes.TaskList1.login1).flashing("error" -> "Invalid username/password")  // only lives until next request
    }
    val result = credentials.getOrElse(Redirect(routes.TaskList1.login1).flashing("error" -> "Major Invalid username/password"))  // is type of  Result
    result
  }

  def createUser1(): Action[AnyContent] = Action { request =>
    val body = request.body.asFormUrlEncoded  //  is type of Option[Map[String, Seq[String]]]
    body.map { args =>   // is type of Option[Result]
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.createUser(username, password))
        Redirect(routes.TaskList1.taskList).withSession("username" -> username)  // save username as session cookie
      else
        Redirect(routes.TaskList1.login1).flashing("error" -> "User creation failed")
    }.getOrElse(Redirect(routes.TaskList1.login1).flashing("error" -> "User creation major failed"))
  }

  def taskList: Action[AnyContent] = Action { implicit request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val tasks = TaskListInMemoryModel.getTasks(username)
      Ok(views.html.taskList1(tasks))
    }.getOrElse(Redirect(routes.TaskList1.login1))
  }

  def logout: Action[AnyContent] = Action {
    Redirect(routes.TaskList1.login1).withNewSession  // clear session cookie
  }

  def addTask(): Action[AnyContent] = Action { request =>
    val usernameOption = request.session.get("username")
    usernameOption.map {username =>
      val body = request.body.asFormUrlEncoded
      body.map { args =>
        val task = args("newTask").head
        TaskListInMemoryModel.addTask(username, task)
        Redirect(routes.TaskList1.taskList)
      }.getOrElse(Redirect(routes.TaskList1.taskList).flashing("error" -> "From addTask - No body"))
    }.getOrElse(Redirect(routes.TaskList1.login1).flashing("error" -> "From addTask - Please login first"))
  }
}
