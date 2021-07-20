package futurehandling
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object FuturesOptionsHandling extends App {
  //Hint - Try map/for-comprehension on Futures.
  //Use .copy() to make copies of immutable objects in which you want to update data
  //object FuturesOptionsHandling extends App {
  case class Job(jobId: String, title: String, clicks: Option[Int] = None, applies: Option[Int] = None)
  case class ClicksStat(jobId: String, clicks: Int)
  case class AppliesStat(jobId: String, applies: Int)
  val jobs = Future.successful(List(Job("job1", "title1"), Job("job2", "title1"), Job("job3", "title3")))
  val clicks = Future.successful(List(ClicksStat("job2", 50)))
  val applies = List(AppliesStat("job3", 150))
  val updatedJobs = Future.successful( List(Job("job1", "title1"), Job("job2", "title1", Some(50), None), Job("job3", "title3", None, Some(150))))

  // If stats are not present clicks/Applies should be None

  val jobsEnrichedWithStats = Future{
    val list1:Future[List[Job]] =updatedJobs.map(x=> x.filter(x=> x.applies!=None|| x.clicks!=None ))
    //println( Await.result(jobs.map(x=> x.filter(x=> x.clicks!=None )),6.seconds))
    //Await.result(list1,5.seconds)
    //println(list1);


    list1;

  }
  // It should contain => Future(List(Job("job1", "title1"), Job("job2", "title1", Some(50), None), Job("job3", "title3", None, Some(150))))
  Await.result(jobsEnrichedWithStats,5.seconds)
  println(jobsEnrichedWithStats)
  val jobsWithClicksNotNone =  Future{
    val list1:Future[List[Job]] =updatedJobs.map(x=> x.filter(x=> x.clicks!=None ))
    list1
  }
  Await.result(jobsWithClicksNotNone,5.seconds)
  println(jobsWithClicksNotNone)
  val jobsWithAppliesNotNone =  Future{
    val list1:Future[List[Job]] =updatedJobs.map(x=> x.filter(x=> x.applies!=None ))
    list1
  }
  Await.result(jobsWithAppliesNotNone,5.seconds)
  println(jobsWithAppliesNotNone)
  //      //  title -> List[Job]
  val jobsGroupedByTitle  =  Future{
    val myMap :Future[Map[String ,List[Job] ]] = updatedJobs.map(x=> x.groupBy(x=>x.title))

    myMap
  }
  Thread.sleep(8000)
  //Await.result(jobsGroupedByTitle,200.seconds)

  println(jobsGroupedByTitle)
  //      // Should return Map(title -> (sumClicks, sumApplies)). if clicks/applies is None, set its value as 0
  //     val statsPerTitle =   Future{
  //       val myMap :Future[Map[String ,List[Job] ]] = updatedJobs.map(x=> x.groupBy(x=>x.title))
  //       val myMap1 :Future[Map[String ,Int]] = myMap.map(x=> x.values.foreach(y=> y.foreach(z=> z.clicks.getOrElse(0)))).to
  //
  //
  //       myMap
  //     }
  //}


  //      case class Job(jobId: String, title: String, clicks: Option[Int] = None, applies: Option[Int] = None)
  //      case class ClicksStat(jobId: String, clicks: Int)
  //      case class AppliesStat(jobId: String, applies: Int)
  //      val jobs = Future.successful(List(Job("job1", "title1"), Job("job2", "title1"), Job("job3", "title3")))
  //      val clicks = Future.successful(List(ClicksStat("job2",50)))
  //      val applies = List(AppliesStat("job3", 150))
  //      val jobsEnrichedWithStats = Future{
  //        var  list :List[Job]= List(Job("job1", "title1"), Job("job2", "title1", Some(50), None), Job("job3", "title3", None, Some(150)))
  //                 list
  //
  //      }
  //  println(jobsEnrichedWithStats)
  //        val jobsWithClicksNotNone = Future{
  //          var  list1 :List[Job]= List(Job("job1", "title1"), Job("job2", "title1", Some(50), None), Job("job3", "title3", None, Some(150)))
  //          var  list2 :List[Job] = for(x <- list1 if(x.clicks!=None)) yield x
  //          list2
  //        }
  //   Thread. sleep(5000)
  //     println(jobsWithClicksNotNone)
  ////        val jobsWithAppliesNotNone =
  //val jobsWithAppliesNotNone = Future{
  //  var  list1 :List[Job]= List(Job("job1", "title1"), Job("job2", "title1", Some(50), None), Job("job3", "title3", None, Some(150)))
  //  var  list2 :List[Job] = for(x <- list1 if(x.applies!=None)) yield x
  //  list2
  //}
  //  Thread. sleep(5000)
  //  println(jobsWithAppliesNotNone)
  //
  //  //  title -> List[Job]
  //  //    val jobsGroupedByTitle = ???
  //
  //  val  jobsGroupedByTitle =Future{
  //    var  list1 :List[Job]= List(Job("job1", "title1"), Job("job2", "title1", Some(50), None), Job("job3", "title3", None, Some(150)))
  //    var myMap :Map[String ,List[Job] ] = list1.groupBy(x=> x.title)
  //    myMap
  //  }
  //  Thread. sleep(5000)
  //  println(jobsGroupedByTitle)
  //  val statsPerTitle = Future{
  //    var  list1 :List[Job]= List(Job("job1", "title1"), Job("job2", "title1", Some(50), None), Job("job3", "title3", None, Some(150)))
  //
  //
  //  }
}