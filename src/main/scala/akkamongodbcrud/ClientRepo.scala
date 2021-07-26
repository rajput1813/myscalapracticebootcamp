package akkamongodbcrud
import scala.concurrent.ExecutionContext.Implicits.global
import akka.util.Timeout
import akkamongodbcrud.ClientRepo.Clients.find
import akkamongodbcrud.ClientRepo.{ClientNotFoundException, Clients, DubiousClientRecordException, db}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY

import scala.language.postfixOps
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase, Observable, Observer}

import scala.concurrent.{Await, Awaitable, Future}
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
case class Client(id:String,var name:String,var inboundFeedUrl:String)
object ClientRepo {

//  var ClientDb =List(Client("1","adeco","abc.json"),
//    Client("2","b","abc.json"),
//    Client("3","c","abc.json"),
//    Client("4","d","abc.json"),
//    Client("5","e","abc.json"),
//    Client("6","f","abc.json"),
//    Client("7","g","abc.json")
//
//  )
  //var ClientDb =List[Client]
  val codecRegistry = fromRegistries(fromProviders(classOf[Client]), DEFAULT_CODEC_REGISTRY)
  val client: MongoClient = MongoClient()
  val db: MongoDatabase = client.getDatabase("myclient").withCodecRegistry(codecRegistry)
  val Clients: MongoCollection[Client] = db.getCollection("clients")
  //Await.result(Clients.insertMany(ClientDb).toFuture(),10 seconds)
//  val data = Await.result(db.getCollection("clients").find(  ).toFuture(),10 seconds)
//  var document :Seq[Document] = Seq.empty[Document]
//  data.foreach(x=>
//    document = document:+x
//  )
//  println(document)


  class ClientNotFoundException extends Throwable("no Client found in database")
  class DubiousClientRecordException extends Throwable("Dubious  Client found in database")
}
trait  ClientRepo {
  def fetchDB() = {
    val data = Await.result(db.getCollection("clients").find().toFuture(), 10 seconds)
    var ClientDb: List[Document] = List.empty[Document]
    data.foreach(x =>
      ClientDb = ClientDb :+ x
    )

    Future {
      ClientDb
    }
  }

  def getClients() = {
    val data = Await.result(Clients.find().toFuture(), 10 seconds)

      Future{

        data.head
      }

  }

  def createClient(id: String, name: String, inboundFeedUrl: String) = {
    var client = Client(id, name, inboundFeedUrl)
    Await.result(Clients.insertOne(client).toFuture(), 10 seconds)
    val data = Await.result(db.getCollection("clients").find().toFuture(), 10 seconds)
    var ClientDb: List[Document] = List.empty[Document]
    data.foreach(x =>
      ClientDb = ClientDb :+ x
    )
    Future {
      ClientDb
    }
  }

  def getClientByid(id: String) = {
    val data = Await.result(Clients.find(equal("id", id)).toFuture(), 10 seconds)
    if(data.isEmpty)
      throw new ClientNotFoundException
    else if(data.length > 1)
      throw new DubiousClientRecordException
      else
  Future {data(0)}

  }


  def updateClientName(id: String, updatedName: String) = {
    val data = Await.result(db.getCollection("clients").updateOne(equal("id", id), set("name", updatedName)).toFuture(), 10 seconds)
    Future{
      data
    }
  }

  def updateClientFeed(id: String, updatedFeed: String) = {
    val data = Await.result(db.getCollection("clients").updateOne(equal("id", id), set("inboundFeedUrl", updatedFeed)).toFuture(), 10 seconds)
   Future{
     data
   }
  }

  def deleteClient(id: String) = {
    implicit val timeout = new Timeout(30 seconds)
    val data = Await.result(db.getCollection("clients").deleteOne(equal("id", id)).toFuture(), 10 seconds)
    fetchDB()


  }

  def queryClient(id: String, name: String) = {
    val data = Await.result(db.getCollection("clients").find(equal("id", id)).toFuture(), 10 seconds)

    data

  }
}
