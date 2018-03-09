package com.gwz.dockerexp.Actors.RaphtoryActors

import java.io.FileWriter
import java.io.File
import akka.actor.Actor

import sys.process._
import scala.language.postfixOps
import java.lang.management.ManagementFactory

import scala.collection.immutable.HashMap

abstract class RaphtoryActor extends Actor {

  def profile():Unit={
    logHeap(getHeap())
    logLiveHeap(getLiveHeap())
  }

  def getHeap():Array[HashMap[String,String]]={
    val PID = ManagementFactory.getRuntimeMXBean().getName().split("@")(0)
    val rawHisto = (s"jmap -histo $PID" !!).split("\n").drop(3)
    rawHisto.take(20).map(x => (x.trim.split("\\s+"))).map(x=> HashMap[String,String](("name",x(3)),("instances",x(1)),("bytes",x(2))))
  }//val sortedHisto = listHisto.toBuffer.sortWith{(a,b) => (a("instances").toInt>b("instances").toInt)}


  def getLiveHeap():Array[HashMap[String,String]]={
    val PID = ManagementFactory.getRuntimeMXBean().getName().split("@")(0)
    val rawHisto = (s"jmap -histo:live $PID" !!).split("\n").drop(3)
    rawHisto.take(20).map(x => (x.trim.split("\\s+"))).map(x=> HashMap[String,String](("name",x(3)),("instances",x(1)),("bytes",x(2))))
  }


  def logHeap(heap: Array[HashMap[String,String]]):Unit={
    val file = new File(s"/logs/${self.path.name}/heapSpace")
    file.mkdirs()
    val fw:FileWriter = new FileWriter(s"/logs/${self.path.name}/heapSpace/${System.currentTimeMillis()}.txt")
    try {
      fw.write("Name,Instances,Bytes")
      heap.foreach(x=>fw.write(s"${x("name")},${x("instances")},${x("bytes")}\n"))
    }
    finally fw.close()
  }

  def logLiveHeap(heap: Array[HashMap[String,String]]):Unit={
    val file = new File(s"/logs/${self.path.name}/liveheapSpace")
    file.mkdirs()
    val fw:FileWriter = new FileWriter(s"/logs/${self.path.name}/liveheapSpace/${System.currentTimeMillis()}.txt")
    try {
      fw.write("Name,Instances,Bytes")
      heap.foreach(x=>fw.write(s"${x("name")},${x("instances")},${x("bytes")}\n"))
    }
    finally fw.close()
  }


}
