package BusinessLogic

import BusinessLogic.Mojo.publishers
import javafx.scene.layout.Priority

import java.util.UUID


case class Client(id:String,name:String,inboundFeedUrl:String,jobGroups:List[JobGroup])
case class JobGroup(id:String,rules:Rule,sponsoredPublisher:List[Publisher],priority:Int)
case class Publisher(id:String,isActive:Boolean,var clientId:String="",var outboundFeed:List[String]=List.empty[String])
case class Job(jobId:String,category:String,title:String,minExperience:Int,priority:String,valid:Boolean)
case class Rule(category:String,minExperience:Int,validity:Boolean)

object Mojo {
//var clients:List[Client]= List(
//  Client(id="c1",name= "client1",inboundFeedUrl = "client1Feed.json",jobGroups = client1[JobGroup]),
//  Client(id="c2",name= "client2",inboundFeedUrl = "client2Feed.json",jobGroups = client2[JobGroup]),
//  Client(id="c3",name= "client3",inboundFeedUrl = "client3Feed.json",jobGroups = client3[JobGroup]),
//  Client(id="c4",name= "client4",inboundFeedUrl = "client4Feed.json",jobGroups = client4[JobGroup]),
//  Client(id="c5",name= "client5",inboundFeedUrl = "client5Feed.json",jobGroups = client5[JobGroup]),
//  Client(id="c6",name= "client6",inboundFeedUrl = "client6Feed.json",jobGroups = client1[JobGroup])
//)
   var jobs :List[Job] =List(
     Job(jobId="j1",category="design",title = "junior designer",minExperience = 2,priority = "low",valid = true),
     Job(jobId="j2",category="design",title = "junior designer",minExperience = 3,priority = "high",valid = false),
     Job(jobId="j3",category="dataScientist",title = "senior",minExperience = 10,priority = "medium",valid = true),
     Job(jobId="j4",category="analyst",title ="senior",minExperience = 5,priority = "low",valid = true),
     Job(jobId="j5",category="design",title = "junior",minExperience = 4,priority = "low",valid = true),
     Job(jobId="j6",category="software",title = "senior",minExperience = 6,priority = "low",valid = true),
     Job(jobId="j7",category="software",title = "junior",minExperience = 8,priority = "medium",valid = true),
     Job(jobId="j8",category="marketing",title = "junior",minExperience = 7,priority = "high",valid = false),
     Job(jobId="j9",category="design",title = "analyst",minExperience = 4,priority = "medium",valid = false),
     Job(jobId="j10",category="marketing",title = "graphic",minExperience = 9,priority = "high",valid = true)
   )
  var publishers =List(
    Publisher(id=UUID.randomUUID().toString,isActive=true),
    Publisher(id=UUID.randomUUID().toString,isActive=true),
    Publisher(id=UUID.randomUUID().toString,isActive=true),
    Publisher(id=UUID.randomUUID().toString,isActive=false)

  )
 def createClient(name:String,inboundFeedUrl:String,pub:List[Int]):Client={
  var id=UUID.randomUUID.toString
  publishers(pub(0)).clientId=id
  publishers(pub(1)).clientId=id
  publishers(pub(0)).outboundFeed= outBoundFeed(name,publishers(pub(0)).clientId)
  publishers(pub(1)).outboundFeed= outBoundFeed(name,publishers(pub(1)).clientId)
  var jobgrp= createJobGroup(jobs,Rule(category="design",minExperience=2,validity=true),publishers=List(publishers(pub(0)),publishers(pub(1))),3,base="category",valid=true)
   val cl=Client(id=id,name=name,inboundFeedUrl=inboundFeedUrl,jobGroups=jobgrp)
   cl
 }
  def createJobGroup (jobs:List[Job],rule:Rule,publishers:List[Publisher],prior: Int,base:String ,valid:Boolean): List[JobGroup] ={
    var jobGroups:List[JobGroup]= List.empty[JobGroup];
    if(base=="category") {
    for(i<- jobs){


       if(i.category==rule.category &&rule.validity==valid){
       // jobGroup.id =
        val jg= JobGroup(id=UUID.randomUUID.toString,rules=rule,sponsoredPublisher =publishers,priority =prior )
        jobGroups= jobGroups :+ jg
      }
     }


    }
    else if(base=="minExperience"){
      for(i<- jobs){


        if(i.minExperience==rule.minExperience &&rule.validity==valid){
          // jobGroup.id =
          val jg= JobGroup(id=UUID.randomUUID.toString,rules=rule,sponsoredPublisher =publishers,priority =prior )
          jobGroups= jobGroups :+ jg
        }
      }

    }
  jobGroups
  }

  def outBoundFeed (cname:String,pId:String):List[String]={
    var title1="outBoundfeed"
    var clientName=cname
    var isActive="true"
    var status ="completed"
    var publishersId=pId
    var Feed:List[String]=List.empty[String]
    Feed=List(title1,clientName,isActive,status,publishersId)
    Feed

  }
def main(Args:Array[String]): Unit ={
  var client:List[Client] =List.empty[Client]
  var j=0;
 for(i<-0 to 5) {
  var name= "client"+i
   var FeedUrl="client"+i+".json"
   var c= createClient(name,FeedUrl,List(j,j+1))
   j=j+1;
   if(j>2)j=0;
   client=client:+ c
 }
  //client.foreach(x=> println(x.jobGroups)
  publishers.foreach(p=>println(p))
 // println(client.foreach(x=>x.jobGroups))

}

}
