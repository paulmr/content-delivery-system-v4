package CDS

import logging.LogCollection

import scala.io.Source
import scala.xml.{Elem, Node}
import scala.xml.parsing.ConstructingParser

/**
  * Created by localhome on 21/10/2016.
  */

object CDSRoute {
  def getFileRequirements(n:Node):Seq[String] = {
    val takeFilesContent = (n \ "take-files").text
    takeFilesContent.split("\\|")
  }

  def getMethodParams(n:Node):Map[String,String] =
    (n.child.collect {
      case e: Elem if e.label != "take-files" =>
        e.label -> e.text
    }).toMap

  def getMethodAttrib(n:Node,attName:String):String =
    n \@ attName match {
      case "" => "(noname)"
      case attr => attr
    }

  def getMethodName(n:Node):String = {
    getMethodAttrib(n,"name")
  }

  def readRoute(x: Node):CDSRoute = {
    val methodList = for {
      child <- x.nonEmptyChildren
      if !child.isAtom
    } yield CDSMethod(child.label,getMethodName(child),getFileRequirements(child),getMethodParams(child))
    CDSRoute(getMethodName(x),getMethodAttrib(x,"type"),methodList)
  }

  def fromFile(filename:String) = {
    val src = Source.fromFile(filename,"utf8")
    val parser = ConstructingParser.fromSource(src,true)

    readRoute(parser.document().docElem)
  }
}

case class CDSRoute(name: String,routetype:String,methods:Seq[CDSMethod]) {
  def dump = {
    println("Got route name " + name + " (" + routetype + ")")
    methods.foreach(x=>{x.dump})
  }

  def runRoute(loggerCollection:LogCollection) = {
    methods.foreach(curMethod=>{curMethod.execute(loggerCollection)})
  }
}
