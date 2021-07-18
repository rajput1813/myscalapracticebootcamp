import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY

import scala.language.postfixOps
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase, Observable, Observer}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
object MongoDbExample {
  def main(args: Array[String]): Unit = {
    val myParcel = Student(
      sender = Address("nepal", "Birgung", "Berlin"),
      name = "rahulsingh",
      rollno = 17115064,
      branch = "CSE",
      clgname = "Nitrr")

    val codecRegistry = fromRegistries(fromProviders(classOf[Student],classOf[Address]), DEFAULT_CODEC_REGISTRY)
    val client: MongoClient = MongoClient()
    val database: MongoDatabase = client.getDatabase("mydb1").withCodecRegistry(codecRegistry)
    val students: MongoCollection[Student] = database.getCollection("student")
    Await.result(students.insertOne(myParcel).toFuture, 10 seconds)
    students.find().foreach(println)
    client.listDatabaseNames().foreach(println)
  }
}
case class Student(sender: Address,

                   name : String,
                   rollno:Int,
                   branch :String,
                   clgname:String
                  )

case class Address(name: String, street: String, city: String)