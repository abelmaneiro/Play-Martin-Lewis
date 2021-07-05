package controllers

import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.{Inject, Singleton}

@Singleton
class React @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def load: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.react.apply())
  }
}
