package akkamongodbcrud

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer


import akka.http.scaladsl.server.Directives._
import scala.io.StdIn

object Server extends App with ClientController {
  implicit val actorSystem= ActorSystem("AkkaHttpServerExample")
  implicit  val materializer =ActorMaterializer
  implicit val executionContext = actorSystem.dispatcher

  val host ="127.0.0.1"
  val port =8080

  lazy  val apiRoutes : Route = pathPrefix("api"){
     clientRoutes
   }


  val httpServerFuture =Http().bindAndHandle(apiRoutes,host,port)
  println(s"server online at http://localhost:8080/\nPress RETURN to Stop...")
  StdIn.readLine()// it will run until user presses return
  httpServerFuture
    .flatMap(_.unbind()) //trigger unbinding from port
    .onComplete(_=> actorSystem.terminate())
}
