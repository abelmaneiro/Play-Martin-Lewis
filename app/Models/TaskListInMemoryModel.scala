package Models

import scala.collection.mutable

object TaskListInMemoryModel {
  private val users = mutable.Map[String, String](
    "Mark" -> "pass"
  )
  private val tasks = mutable.Map[String, List[String]](
    "Mark"  -> List("Make videos", "eat", "sleep", "code")
  )

  def validateUser(username: String, password: String): Boolean = {
    // users.get(username).map(_ == password).getOrElse(false)
    // users.get(username).exists(_ == password)
    users.get(username).contains(password)
  }

  def createUser(username: String, password: String): Boolean = {
    if (users.contains(username)) false
    else {
      // users += (username -> password)
      users(username) = password
      tasks(username) = Nil
      true
    }
  }

  def getTasks(username: String): Seq[String] = {
    //tasks.get(username).getOrElse(List())
    tasks.getOrElse(username, Nil)
  }

  def addTask(username: String, task: String): Unit = ???

  def removeTask(username: String, index: Int): Boolean = ???

}
