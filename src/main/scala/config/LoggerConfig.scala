package config
import scala.collection.JavaConversions._

/**
  * Created by localhome on 26/10/2016.
  */
object LoggerConfig {
  def newLoggerConfig(params:java.util.Map[String,String]):LoggerConfig = {
    val newparams = params.toMap[String,String]

    LoggerConfig(newparams("name"),newparams("enabled").toBoolean,
      newparams.filter(kvtuple=>
        kvtuple._1 match {
          case "name"=>false
          case "enabled"=>false
          case _=>true
        }
      )
    )
  }
}

case class LoggerConfig(name: String, enabled: Boolean, params: Map[String,String]) {
  def dump = {
    println("\tLogger name: "+name)
    println("\tEnabled? " + enabled)
    println("\tOther parameters: " + params)
  }
}
