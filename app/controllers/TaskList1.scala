package controllers

import Models.TaskListInMemoryModel
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton  // Juice injection which Play used by default for dependency injection
class TaskList1 @Inject()(cc: ControllerComponents) extends AbstractController(cc) { // need to inject the
                                                            // Controller Components and extend the Controller class
  def login1: Action[AnyContent] = Action {
    Ok(views.html.login1())
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
        Redirect(routes.TaskList1.login1)
    }
    val result = credentials.getOrElse(Redirect(routes.TaskList1.login1))  // is type of  Result
    result
  }

  def createUser1: Action[AnyContent] = Action { request =>
    val body = request.body.asFormUrlEncoded  //  is type of Option[Map[String, Seq[String]]]
    body.map { args =>   // is type of Option[Result]
      val username = args("username").head
      val password = args("password").head
      if (TaskListInMemoryModel.createUser(username, password))
        Redirect(routes.TaskList1.taskList).withSession("username" -> username)  // save username as session cookie
      else
      Redirect(routes.TaskList1.login1)
    }.getOrElse(Redirect(routes.TaskList1.login1))
  }

  def taskList: Action[AnyContent] = Action { request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val tasks = TaskListInMemoryModel.getTasks(username)
      Ok(views.html.taskList1(tasks))
    }.getOrElse(Redirect(routes.TaskList1.login1))
  }

  def logout = Action { request =>
    Redirect(routes.TaskList1.login1).withNewSession  // clear session cookie
  }

}
