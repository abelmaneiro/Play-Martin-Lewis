package controllers

import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton  // Juice injection which Play used by default for dependency injection
class TaskList1 @Inject()(cc: ControllerComponents) extends AbstractController(cc) { // need to inject
  // the Controller Components and extend the Controller class
  def login1: Action[AnyContent] = Action {
    Ok(views.html.login1())
  }
  def validateLogin1Get(username: String, password: String): Action[AnyContent] = Action {
    Ok(s"$username login with $password")
  }

  def taskList: Action[AnyContent] = Action {
    Ok(views.html.taskList1(Seq("Task1", "Task2", "Task3")))   // Because it's a html view it's in the html folder
  }
}
