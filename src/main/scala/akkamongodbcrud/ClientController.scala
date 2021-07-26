package akkamongodbcrud
import akka.http.scaladsl.unmarshalling.{ FromEntityUnmarshaller, FromRequestUnmarshaller, Unmarshaller }
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.pathPrefix
import akkamongodbcrud.ClientController.{UpdateFeed, UpdateName}
import org.mongodb.scala.bson.collection.mutable.Document
import spray.json.DefaultJsonProtocol.immSeqFormat
//import akkamongodbcrud.ClientRepo.Client
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akkamongodbcrud.ClientController.QueryClient
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import scala.util.{Failure, Success}


object ClientController {
case class QueryClient( id:String,
                        name:String
                      )
  case class UpdateName(id:String,name:String)
  case class UpdateFeed(id:String,inboundFeedUrl:String)
  object ClientJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
    import spray.json._
    implicit val printer = PrettyPrinter
    implicit val clientFormat :RootJsonFormat[Client]= jsonFormat3(Client)
    implicit val  ClientQueryFormat:RootJsonFormat[QueryClient] =jsonFormat2(QueryClient)
    implicit val updateNameFormat:RootJsonFormat[UpdateName] =jsonFormat2(UpdateName)
    implicit val updateFeedFormat:RootJsonFormat[UpdateFeed] =jsonFormat2(UpdateFeed)

  }
}
trait ClientController extends ClientRepo{
  implicit def actorSystem:ActorSystem
  lazy val logger = Logging(actorSystem,classOf[ClientController])
  import ClientController.ClientJsonProtocol._
  import ClientRepo._
  val cdirective = pathPrefix("client")
  lazy val clientRoutes: Route = pathPrefix("client"){
    get{
      println("here")

      pathPrefix("search") {

        path("all") {
          onComplete(getClients()) {
            _ match {
              case Success(client) =>
                logger.info("got the client records given client id is")
                complete(StatusCodes.OK,client)
              case Failure(throwable) =>
                logger.error(s"Failed to get the employee record given the employee id ")
                throwable match {
                  case e: ClientNotFoundException => complete(StatusCodes.NotFound, "No client found")
                  case e: DubiousClientRecordException => complete(StatusCodes.NotFound, "dubious  client found")
                  case _ => complete(StatusCodes.InternalServerError, "fail to get the Client")
                }
            }
          }


        }
      }
    } ~get{
      pathPrefix("query"){
        path(Segment) { id =>
          onComplete(getClientByid(id)) {
            _ match {
              case Success(client) =>
                logger.info("got the client records given client")
                complete(StatusCodes.OK,client)
              case Failure(throwable) =>
                logger.error(s"Failed to get the client record ")
                throwable match {
                  case e: ClientNotFoundException => complete(StatusCodes.NotFound, "No client found")
                  case e: DubiousClientRecordException => complete(StatusCodes.NotFound, "dubious  client found")
                  case _ => complete(StatusCodes.InternalServerError, "fail to get the Client")
                }
            }
          }


        }

      }
    }~
       post{
      pathPrefix("create"){

        entity(as[Client]){q=>
          pathEndOrSingleSlash{
            onComplete(createClient(q.id,q.name,q.inboundFeedUrl)){
              _ match {
                case Success(client) =>
                  logger.info("Got the employee records for the search query. ")
                  complete(StatusCodes.OK)
                case Failure(_) =>
                  logger.error("failed to get the client with given querycondition.")
                  complete(StatusCodes.NotFound,"failed to query the Employee.")
              }
            }
          }

        }
      }
    }~ delete {
         pathPrefix("delete") {

           path(Segment) { id =>
             onComplete(deleteClient(id)) {
               _ match {
                 case Success(id) =>
                   logger.info(s"got the client record")
                   complete(StatusCodes.OK, s"client deleted ")
                 case Failure(throwable) =>
                   logger.error(s"Failed to get the employee record given the employee id $id")
                   throwable match {
                     case e: ClientNotFoundException => complete(StatusCodes.NotFound, "No client found")
                     case e: DubiousClientRecordException => complete(StatusCodes.NotFound, "dubious  client found")
                     case _ => complete(StatusCodes.InternalServerError, "fail to get the Client")
                   }
               }
             }


           }
         }
       }~put {
      pathPrefix("name"){
        entity(as[UpdateName]){ q=>
          pathEndOrSingleSlash{
            onComplete( updateClientName(q.id,q.name)){
              _ match {
                case Success(client) =>
                  logger.info("Got the employee records for the search query. ")
                  complete(StatusCodes.OK,"client Name updated ")
                case Failure(_) =>
                  logger.error("failed to get the client with given querycondition.")
                  complete(StatusCodes.InternalServerError,"failed to query the Employee.")
              }
            }
          }

        }
      }~ pathPrefix("feed"){
        entity(as[UpdateFeed]){ q=>
          pathEndOrSingleSlash{
            onComplete( updateClientFeed(q.id,q.inboundFeedUrl)){
              _ match {
                case Success(client) =>
                  logger.info("Got the employee records for the search query. ")
                  complete(StatusCodes.OK,"feed updated")
                case Failure(throwable) =>
                  logger.error("failed to get the client with given querycondition.")
                  complete(StatusCodes.InternalServerError,"failed to query the Employee.")
              }
            }
          }

        }
      }
    }
  }
}

