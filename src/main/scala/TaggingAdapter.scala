import Agg.Created
import akka.persistence.journal.{EventAdapter, EventSeq, Tagged}

class TaggingAdapter extends EventAdapter {

  override def manifest(event: Any): String = ""

  override def toJournal(event: Any): Any = event match {
    case evt: Created =>
      Tagged(evt, Set(Agg.tag))
  }

  override def fromJournal(event: Any, manifest: String): EventSeq =
    event match {
      case evt: Created =>
        EventSeq.single(evt)
    }
}
