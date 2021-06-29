package controllers

import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton
class TaskList2 @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def load: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.version2Main())
  }

  def login: Action[AnyContent] = Action { request =>
    Ok(views.html.login2())
  }
}
