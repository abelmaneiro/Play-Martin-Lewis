package actors

import akka.actor.{Actor, ActorRef, Props}

class ChatActor(out: ActorRef, manager: ActorRef) extends Actor {
  println("ChatActor create: " + self.toString)
  manager ! ChatManager.NewChatter(self)

  import ChatActor._
  override def receive: Receive = {
    case s: String =>
      println("CharActor.String - sending to Manager:"+ s)
      manager ! ChatManager.Message(s)
    case SendMessage(msg) =>
      println("CharActor.SendMessage - sending to client:"+ msg)
      out ! msg
    case m => println("Unhandled message in ChatActor: + m")
  }
}

object ChatActor {
  def props(out: ActorRef, manager:ActorRef) = Props.apply(new ChatActor(out, manager))

  case class SendMessage(msg: String)
}
