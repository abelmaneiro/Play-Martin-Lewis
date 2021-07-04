package controllers

import models.{TaskListInMemoryModel, UserData}
import play.api.libs.json.{JsError, JsResult, JsSuccess, JsValue, Json, Reads}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton
class TaskList3 @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def load: Action[AnyContent] = Action { implicit request =>
    Ok.apply(views.html.version3Main.apply())
  }

  implicit val userDataReads: Reads[UserData] = Json.reads[UserData] //  How to decode/read JSON into a value

  def validate: Action[AnyContent] = Action { implicit request =>
    val bodyOption: Option[JsValue] = request.body.asJson
    bodyOption.map { body =>
      val jsData: JsResult[UserData] = Json.fromJson[UserData](body)  // need to include the type
       jsData match {
        case JsSuccess(data, path) =>
          if (TaskListInMemoryModel.validateUser(data.username, data.password)) {
            Ok(Json.toJson(true))
              .withSession("username" -> data.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
          } else {
            Ok(Json.toJson(false))
          }
        case e @ JsError(errors) => Redirect(routes.TaskList3.load)
       }
    }.getOrElse(Redirect(routes.TaskList3.load))
  }

  def taskList: Action[AnyContent] = Action { request =>
    request.session.get("username").map { username =>
      Ok(Json.toJson(TaskListInMemoryModel.getTasks(username)))
    }.getOrElse(Ok(Json.toJson(Seq.empty[String])))
  }

  def addTask(): Action[AnyContent] = Action { request =>
    request.session.get("username").map { username =>
      request.body.asJson.map { body =>
        Json.fromJson[String](body) match {  // need to include the type
          case JsSuccess(task, _) =>
            TaskListInMemoryModel.addTask(username, task)
            Ok(Json.toJson(true))
          case e @ JsError(_) => Redirect(routes.TaskList3.load)
        }
      }.getOrElse(Ok(Json.toJson(false)))
    }.getOrElse(Ok(Json.toJson(false)))
  }

  def deleteTask(): Action[AnyContent] = Action { request =>
    request.session.get("username").map { username =>
      request.body.asJson.map { body =>
        Json.fromJson[Int](body) match {  // need to include the type
          case JsSuccess(index, _) =>
            TaskListInMemoryModel.removeTask(username, index)
            Ok(Json.toJson(true))
          case e @ JsError(_) => Redirect(routes.TaskList3.load)
        }
      }.getOrElse(Ok(Json.toJson(false)))
    }.getOrElse(Ok(Json.toJson(false)))
  }

  def createUser(): Action[AnyContent] = Action { implicit request =>
    request.body.asJson.map { body =>
      Json.fromJson[UserData](body) match {
        case JsSuccess(userData, _) =>
          if (TaskListInMemoryModel.createUser(userData.username, userData.password)) {
            Ok(Json.toJson(true))
              .withSession("username" -> userData.username, "csrfToken" -> play.filters.csrf.CSRF.getToken.get.value)
          } else {
            Ok(Json.toJson(false))
          }
        case e @ JsError(_) => Redirect(routes.TaskList3.load)
      }
    }.getOrElse(Ok(Json.toJson(false)))
  }

  def logout: Action[AnyContent] = Action { request =>
    Ok(Json.toJson(true)).withNewSession
  }

  def data: Action[AnyContent] = Action {
    Ok.apply(Json.toJson(Seq("a", "b", "c")))
  }

}
