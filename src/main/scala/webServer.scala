
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentType, ContentTypes, HttpEntity, HttpMethod, HttpMethods}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.io.StdIn
object webServer {
  def main(args: Array[String]){
    implicit val system= ActorSystem()
    implicit  val materializer =ActorMaterializer
    implicit val executionContext = system.dispatcher
    //path =>http://localhost:8080/
    lazy val route =
      get {
        pathSingleSlash{
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,"<html><body>hello world!</body></html>"))
        } ~
          path("ping"){
            complete("pong")
          }~
          path("crash"){
            complete("boom")
          }
      }

    val route2 =
      concat(
        get{
          complete("received Getjnjnj")
        },
        complete("received somthingh else")
      )
    val route1:Route= {
      ctx=>
        if(ctx.request.method==HttpMethods.GET)
          ctx.complete("Received GEThghh5566")
        else
          ctx.complete("received somthing else1233")
    }
    val route3 =
      get {
        complete("Received GETnkn")
      } ~
        complete("RECEIVED Somethingelse")

    //route will be implicitly converted to 'flow' using routeresulr.route2Handerflow'
    val bindingFuture =Http().bindAndHandle(route3,"Localhost",8080)

    println(s"server online at http://localhost:8080/\nPress RETURN to Stop...")
    StdIn.readLine()// it will run until user presses return
    bindingFuture
      .flatMap(_.unbind()) //trigger unbinding from port
      .onComplete(_=> system.terminate())
  }
}
