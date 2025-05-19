package Project

import java.time.Instant
import java.util.logging.{FileHandler, Formatter, LogRecord, Logger, Level}

object log {

  private var initialized = false

  def setup(): Logger = {
    val logger: Logger = Logger.getLogger("Discount45Logger")
    if (!initialized) {
      // Add file handler for logging to file
      val fileHandler = new FileHandler("discount45.log", true)
      // Custom formatter: TIMESTAMP LOGLEVEL MESSAGE
      fileHandler.setFormatter(new Formatter {
        override def format(record: LogRecord): String =
          s"${Instant.ofEpochMilli(record.getMillis)} ${record.getLevel.getName} ${record.getMessage}\n"
      })
      logger.addHandler(fileHandler)
      logger.setLevel(Level.ALL)
      initialized = true
    }
    logger
  }
}