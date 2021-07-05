package actors

import akka.actor.{Actor, ActorRef}

class ChatManager extends Actor {
  println("ChatManger created " + self.toString())
  //noinspection ActorMutableStateInspection
  private var chatters = List.empty[ActorRef]

  import ChatManager._
  override def receive: Receive = {
    case NewChatter(chatter) =>
      println("ChatManager.NewChatter - adding to list " + chatter.toString())
      chatters ::= chatter
    case Message(msg) =>
      println("ChatManager.Message -  sending to all ChatActors message:" + msg)
      for (c <- chatters) c ! ChatActor.SendMessage(msg)
    case m => println("Unhandled message in ChatManager:+" + m)
  }
}

object ChatManager {
  case class NewChatter(chatter: ActorRef)
  case class Message(msg: String)
}