import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import scala.language.postfixOps
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase, Observable, Observer}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

case class Employee(_id: String, name: String, dateOfBirth: String)
object MongoDbExample  {
  def main(args: Array[String]): Unit = {
    val people: Seq[Student]  = Seq(Student(sender = Address("nepal", "Birgung", "Berlin"), name = "rahulsingh", rollno = 17115064, branch = "CSE",
      clgname = "Nitrr"),
     Student(sender = Address("a", "B", "c"), name = "a", rollno = 1, branch = "CSE",
      clgname = "Nitrr"),
     Student(sender = Address("r", "g", "Bb"), name = "singh", rollno = 12, branch = "ece",
      clgname = "Nitrr")
    )
val people1 :Seq[Employee]=Seq(
  Employee(_id="1",name="ram",dateOfBirth="12021986"),
  Employee(_id="2",name="raj",dateOfBirth="12021986")
)
    val codecRegistry = fromRegistries(fromProviders(classOf[Student],classOf[Address],classOf[Employee]), DEFAULT_CODEC_REGISTRY)
    val client: MongoClient = MongoClient()
    val database: MongoDatabase = client.getDatabase("myclient").withCodecRegistry(codecRegistry)
    val students: MongoCollection[Employee] = database.getCollection("clients")
   // Await.result(students.insertMany(people).toFuture(),10 seconds)
  // val allStudent= Await.result(database.getCollection("student").find().first().toFuture(),10 seconds)
   // allStudent.foreach(println)
  //  Await.result(students.updateOne(equal("name", "singh"), set("rollno", 14)).toFuture(),10 seconds)
   // Await.result(students. deleteOne(equal("name", "a")).toFuture(),10 seconds)
    //Await.result(students.insertMany(people1).toFuture(),10 seconds)

  }
}
case class Student(sender: Address,

                   name : String,
                   rollno:Int,
                   branch :String,
                   clgname:String
                  )

case class Address(name: String, street: String, city: String)