import Agg.{Create, Created}
import akka.persistence.PersistentActor

import scala.util.Random

object Agg {

  // Random tag, so one does not have to drop keyspace
  val tag = Random.alphanumeric.take(6).mkString("") 

  case class Create(v: String)

  case class Created(v: String)

}

class Agg extends PersistentActor {

  var state: String = ""

  override def persistenceId: String = s"agg-${self.path.name}"

  override def receiveRecover: Receive = {
    case Created(x) =>
      state = x
  }

  override def receiveCommand: Receive = {
    case Create(v) =>
      persist(Created(v))(_ => ())
  }

}
