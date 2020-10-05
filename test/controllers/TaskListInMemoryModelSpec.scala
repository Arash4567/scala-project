import org.scalatestplus.play._
import models._
class TaskListInMemoryModelSpec extends PlaySpec {
    "TaskListInMemoryModel" must {
        "do valid login for default user" in {
            TaskListInMemoryModel.validateUser("kali", "code") mustBe(true)
        }
        "reject login with wrong password" in {
          TaskListInMemoryModel.validateUser("kali", "coder") mustBe(false)  
        }
        "reject login with wrong username" in {
          TaskListInMemoryModel.validateUser("kalikas", "code") mustBe(false)  
        }
        "reject login with wrong username and password" in {
          TaskListInMemoryModel.validateUser("kalikas", "coder") mustBe(false)  
        }
        "get correct default tasks" in {
            TaskListInMemoryModel.getTasks("kali") mustBe(List("hello","hi","greet"))
        }
        "create new user with no tasks" in {
            TaskListInMemoryModel.createUser("ubuntu", "coder") mustBe(true)
            TaskListInMemoryModel.getTasks("ubuntu") mustBe(Nil)
        }
        "create new user with exsiting name" in {
            TaskListInMemoryModel.createUser("kali", "passcode") mustBe(false)
        }
        "add new task for default user" in {
            TaskListInMemoryModel.addTask("kali", "testing")
            TaskListInMemoryModel.getTasks("kali") must contain ("testing")
        }
        "add new task for new user" in {
            TaskListInMemoryModel.addTask("ubuntu", "testing1")
            TaskListInMemoryModel.getTasks("ubuntu") must contain ("testing1")
        }
        "remove task from default user" in {
            TaskListInMemoryModel.removeTask("kali", TaskListInMemoryModel.getTasks("kali").indexOf("hi"))
            TaskListInMemoryModel.getTasks("kali") must not contain  ("hi")
        }
    }
}

