package controllers

import models.TaskListInMemoryModel
import play.api.data.Forms.text
import play.api.data.{Form, Forms}
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents}

import javax.inject.{Inject, Singleton}

case class LoginData(username: String, password: String)

@Singleton  // Juice injection which Play used by default for dependency injection
class TaskList1 @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) { // need to inject the
  // Controller Components and extend the Controller class. In addition need to use the Message version for Play forms

  val loginForm: Form[LoginData] = Form.apply(Forms.mapping(   // mapping from field -> data type
    "Username"-> Forms.text(3, 10),    // Min & max text field
    "Password" -> text(8)
  ) (LoginData.apply)(LoginData.unapply))  // methods to construct and deconstruct the data

  // def login1: Action[AnyContent] = Action {request =>
  //  Ok(views.html.login1()(request))
  // }
  def login1: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.login1(loginForm))  // By making request implicit, no need to pass it in, but need to pass in loginForm
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

  def createUserForm(): Action[AnyContent] = Action { implicit request =>
    loginForm.bindFromRequest().fold( // bind the loginForm to the implicit request. .fold either calls the error or success code
      formWithError => BadRequest(views.html.login1(formWithError)), // error code
      loginData => // success code
        if (TaskListInMemoryModel.createUser(loginData.username, loginData.password))
          Redirect(routes.TaskList1.taskList).withSession("username" -> loginData.username)
        else
          Redirect(routes.TaskList1.login1).flashing("error" -> "Invalid username/password")
    )
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

   def deleteTask(): Action[AnyContent] = Action { request =>
    val usernameOption = request.session.get("username")
    usernameOption.map { username =>
      val body = request.body.asFormUrlEncoded
      body.map { args =>
        val idx = args("index").head.toInt
        TaskListInMemoryModel.removeTask(username, idx)
        Redirect(routes.TaskList1.taskList)
      }.getOrElse(Redirect(routes.TaskList1.taskList).flashing("error" -> "From removeTask - No body"))
    }.getOrElse(Redirect(routes.TaskList1.login1).flashing("error" -> "From removeTask - Please login first"))
  }
}
