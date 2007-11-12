package org.hackystat.utilities.logger;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Provides a convenience method for Restlet logging that adjusts the output Handlers.
 * @author Philip Johnson
 */
public class RestletLoggerUtil {
  
  /**
   * Adjusts the Restlet Loggers so that they send their output to a file, not the console. 
   * @param serviceDir The directory within .hackystat that this data will be sent to.
   */
  public static void useFileHandler(String serviceDir) {
    LogManager logManager = LogManager.getLogManager();
    for (Enumeration<String> en = logManager.getLoggerNames(); en.hasMoreElements() ;) {
      String logName = en.nextElement();
      if (logName.startsWith("com.noelios") ||
          logName.startsWith("org.restlet")) {
        // First, get rid of current Handlers
        Logger logger = logManager.getLogger(logName);
        logger = logger.getParent();
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
          logger.removeHandler(handler);
        }
        // Define a file handler that writes to the ~/.hackystat/<service>/logs directory
        File logDir = new File(System.getProperty("user.home") + 
            "/.hackystat/" + serviceDir + "/logs/");
        logDir.mkdirs();
        String fileName = logDir + "/" + logName + ".%u.log";
        FileHandler fileHandler;
        try {
          fileHandler = new FileHandler(fileName, 500000, 1, true);
          fileHandler.setFormatter(new SimpleFormatter());
          logger.addHandler(fileHandler);
        }
        catch (IOException e) {
          throw new RuntimeException("Could not open the log file for this Hackystat service.", e);
        }
      }
    }
  }
  

}
