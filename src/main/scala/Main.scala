import java.util.concurrent.Executors

import Agg.Create
import akka.actor.{ActorSystem, Props}
import akka.persistence.cassandra.query.scaladsl.CassandraReadJournal
import akka.persistence.query.{PersistenceQuery, TimeBasedUUID}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

object Main extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(100))

  val N = 10000

  val cassandraReadJournal =
    PersistenceQuery(system)
      .readJournalFor[CassandraReadJournal](CassandraReadJournal.Identifier)

  def createAgg(i: Int) = {
    val s = f"$i%06d-${Agg.tag}"
    val actor = system.actorOf(Props(new Agg))
    actor ! Create(s)
  }

  def read(low: Int, high: Int) = {
    val n = high - low + 1
    cassandraReadJournal
      .eventsByTag(Agg.tag, TimeBasedUUID(cassandraReadJournal.firstOffset))
      .zipWith(Source(low to high)) {
        case (e, i) =>
          println(s"$i: read side: $e")
          e
      }
      .take(n)
      .takeWithin(1.minute)
      .runWith(Sink.seq)
      .map(es => println(s"Read ${es.size} of expected $n events."))
  }

  println(s"Tag is ${Agg.tag}")
  //Just to initialize, it turns out that this is important in runtime.
  createAgg(0)
  Await.ready(read(0, 0), 1.minute)
  //Agg 0 - is ready and visible
  read(0, N) // Start reading all events (Agg 0 included, it has same tag).
  Thread.sleep(1000) // CassandraReadQuery should be ready  
  (1 to N).foreach(createAgg) // Create burst of Aggs
}
