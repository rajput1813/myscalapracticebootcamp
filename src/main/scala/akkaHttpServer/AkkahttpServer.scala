package akkaHttpServer
import akka.http.scaladsl.model.{ContentType, ContentTypes, HttpEntity, HttpMethod, HttpMethods, StatusCodes}
import akka.actor.ActorSystem
import akka.actor.Status.{Failure, Success}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol
import spray.json._
import com.fasterxml.jackson.core.PrettyPrinter

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn


class DonutRoutes extends JsonSupport{
  def route():Route ={
    val donutDao =new DonutDao()
    path("create-donut"){
      post{
        entity(as[Donut]){ donut=>
          complete(StatusCodes.Created,s"Created donut = $donut")

        }
      }~ delete{
        complete(StatusCodes.MethodNotAllowed,"HTTP Delete operation is not allowed for the create-donut-path.")
      }

    }~ path("donuts"){
      get {
        onSuccess(donutDao.fetchDonuts()){ donuts =>
          complete(StatusCodes.OK,donuts)
        }
      }

    }
  }

}
class ServerVersion extends JsonSupport {
  def route():Route ={
    path("server-version"){
      get{
        val serverVersion ="1.0.0.0"
        complete(
         HttpEntity( ContentTypes.`text/html(UTF-8)`,serverVersion))
      }
    }
  }

  def routeAsJson():Route = {
    path("server-version-json"){
      get{
        val jsonResponse =
          """
            |{
            | "app": "Akka HTTP REST Server",
            | "version": "1.0.0.0"
            |}
          """
            .stripMargin
            complete(HttpEntity(ContentTypes.`application/json`,jsonResponse))
      }
    }
  }
  def routeAsJsonEncoding():Route = {
    path("server-version-json-encoding"){
      get{
        val server = AkkaHttpRestServer("Akka HTTP Rest Server","1.0.0.1")
        complete(server)
      }
    }
  }
}

object AkkaHttpServer extends App  {
  implicit val system= ActorSystem()
  implicit  val materializer =ActorMaterializer
  implicit val executionContext = system.dispatcher

  val host ="127.0.0.1"
  val port =8080
//  val routes : Route = {
//    get{
//      complete("Akka HTTP Server is Up")
//    }
//
//  }
val serverUpRoute:Route = get{
  complete("Akka Http Server is Up. ")
}
  val serverVersion=new ServerVersion()
  val serverVersionRoute = serverVersion.route()
  val serverVersionRouteAsJson =serverVersion.routeAsJson()
  val serverVersionJsonEncoding = serverVersion.routeAsJsonEncoding()
  val donutRoutes = new DonutRoutes().route();

  val routes: Route =donutRoutes~ serverVersionRoute  ~serverVersionRouteAsJson ~ serverVersionJsonEncoding~serverUpRoute

  val httpServerFuture =Http().bindAndHandle(routes,host,port)
//  httpServerFuture.onComplete{
//    case Success(binding)=>
//
//      println("binding is succesfull")
//    case Failure(e)=>
//      println(s"server failed $e")
//
//  }

  println(s"server online at http://localhost:8080/\nPress RETURN to Stop...")
  StdIn.readLine()// it will run until user presses return
  httpServerFuture
    .flatMap(_.unbind()) //trigger unbinding from port
    .onComplete(_=> system.terminate())
}
