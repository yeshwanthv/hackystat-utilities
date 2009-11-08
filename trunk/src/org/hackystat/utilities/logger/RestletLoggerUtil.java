package org.hackystat.utilities.logger;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.hackystat.utilities.home.HackystatUserHome;

/**
 * Provides a convenience method for Restlet logging that adjusts the output Handlers.
 * @author Philip Johnson
 */
public final class RestletLoggerUtil {
  
  /** Make this class noninstantiable. */
  private RestletLoggerUtil() {
    // Do nothing.
  }
  
  /**
   * Returns true if logName is a Restlet logger in logManager.
   * @param logName The logger name.
   * @return True if it's a Restlet Logger.
   */
  private static boolean isRestletLoggerName(String logName) {
    LogManager logManager = LogManager.getLogManager();
    return 
    ((logName.startsWith("com.noelios") || 
      logName.startsWith("org.restlet") || 
        "global".equals(logName)) 
        &&
       (logManager.getLogger(logName) != null));
  }
  
  /**
   * Adjusts the Restlet Loggers so that they send their output to a file, not the console. 
   * @param serviceDir The directory within .hackystat that this data will be sent to.
   */
  public static void useFileHandler(String serviceDir) {
    LogManager logManager = LogManager.getLogManager();
    //System.out.println("In useFileHandler");
    for (Enumeration<String> en = logManager.getLoggerNames(); en.hasMoreElements() ;) {
      String logName = en.nextElement();
      //System.out.println("logName is: '" + logName + "'");
      if (isRestletLoggerName(logName)) {
        // First, get rid of current Handlers
        Logger logger = logManager.getLogger(logName);
        //System.out.println("logger is: " + logger);
        logger = logger.getParent();
        //System.out.println("parent logger is: " + logger);
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
          logger.removeHandler(handler);
        }
        //System.out.println("Removed handlers.");
        // Define a handler that writes to the ~/.hackystat/<service>/logs directory
        File logDir = new File(HackystatUserHome.getHome(), 
            ".hackystat/" + serviceDir + "/logs/");
        boolean dirsOk = logDir.mkdirs();
        if (!dirsOk && !logDir.exists()) {
          throw new RuntimeException("mkdirs() failed");
        }
        //System.out.println("Made this directory: " + logDir);
        String fileName = logDir + "/" + logName + ".%g.%u.log";
        FileHandler fileHandler;
        try {
          fileHandler = new FileHandler(fileName, 500000, 10, true);
          fileHandler.setFormatter(new SimpleFormatter());
          logger.addHandler(fileHandler);
        }
        catch (IOException e) {
          //throw new RuntimeException
          // ("Could not open the log file for this Hackystat service.", e);
          System.out.println("Could not open log file: " + fileName + " " + e.getMessage());
        }
      }
    }
  }
  
  /**
   * Disables all Restlet-based loggers.
   */
  public static void disableLogging() {
    LogManager logManager = LogManager.getLogManager();
    for (Enumeration<String> en = logManager.getLoggerNames(); en.hasMoreElements() ;) {
      String logName = en.nextElement();
      if (isRestletLoggerName(logName)) {
        Logger logger = logManager.getLogger(logName);
        logger = logger.getParent();
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
          logger.removeHandler(handler);
        }
      }
    }
  }
}
