package controllers

import actors.{ChatActor, ChatManager}
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, WebSocket}

import javax.inject.{Inject, Singleton}

@Singleton
class WebSocketChat @Inject() (cc: ControllerComponents) (implicit system: ActorSystem, mat: Materializer) //Akka implicits
  extends AbstractController(cc) {

  private val manager = system.actorOf(Props.apply[ChatManager](), "Manager")

  def index: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.chatPage.apply())
  }

  def socket = WebSocket.accept[String, String] { request =>  //Open server Socket input of String & output of String
    println("Incoming connection")
    ActorFlow.actorRef { out  =>  // Create a flow that is handled by ChatActor.
      ChatActor.props(out, manager)  // creates CharActor
    }
  }
}
