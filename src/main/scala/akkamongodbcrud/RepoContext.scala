package akkamongodbcrud

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

object RepoContext {
  lazy  val actorSystem= ActorSystem("RepositoryContext")
  implicit  val materializer =ActorMaterializer
  lazy  val scheduler= actorSystem.scheduler
  implicit lazy val executionContext = actorSystem.dispatcher


}
