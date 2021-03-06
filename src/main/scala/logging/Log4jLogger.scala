package logging
import CDS.CDSMethod
import org.apache.logging.log4j.LogManager

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Simple logger implementation with log4j
  */
class Log4jLogger extends Logger {
  val l4j = LogManager.getLogger("route")

  override def init: Boolean = true

  override def relayMessage(msg: String, curMethod: CDSMethod, severity: String): Unit = {
    Future {
      val logstring = curMethod.name + ": " + msg
      severity match {
        case "log" => l4j.info(logstring)
        case "error" => l4j.error(logstring)
        case "warn" => l4j.warn(logstring)
        case "debug" => l4j.debug(logstring)
        case _ => l4j.debug(logstring)
      }
    }
  }

  override def teardown: Boolean = true
}
