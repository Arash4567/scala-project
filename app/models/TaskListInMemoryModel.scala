package models

object TaskListInMemoryModel {
    private val users = Map[String, String]("kali" -> "code")
    private val tasks = Map[String, List[String]]("kali" -> List("hello","hi","greet"))

  def validateUser(username: String, password: String): Boolean = {
      users.get(username).map(_ == password).getOrElse(false)
  }

  def createUser(username: String, password: String): Boolean = ???
  
  def getTasks(username: String): Seq[String] = {
      tasks.get(username).getOrElse(Nil)
  }
  
  def addTask(username: String, task: String): Unit = ???
  
  def removeTask(username: String, index: Int): Boolean = ???
}
