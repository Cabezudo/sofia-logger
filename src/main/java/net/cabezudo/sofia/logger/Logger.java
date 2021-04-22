package net.cabezudo.sofia.logger;

import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2018.08.15
 */
public class Logger {

  private static final DateFormat sdf = DateFormat.getDateTimeInstance();

  private static Level actualLevel = Level.SEVERE;

  private Logger() {
    // Nothing to do here
  }

  private static void log(Level messageLevel, Thread currentThread, String message, Object... parameters) {
    if (messageLevel.ordinal() < Logger.actualLevel.ordinal()) {
      return;
    }
    Date date = new Date();
    StackTraceElement[] stacktrace = currentThread.getStackTrace();
    StackTraceElement ste = stacktrace[3];
    String className = ste.getClassName();
    String methodName = ste.getMethodName();
    int lineNumber = ste.getLineNumber();
    String metadata = className + ":" + methodName + ":" + lineNumber;

    String fullMessage;
    if (parameters.length == 0) {
      fullMessage = sdf.format(date) + " [" + metadata + "] " + message;
    } else {
      fullMessage = sdf.format(date) + " [" + metadata + "] " + String.format(message, parameters);
    }
    System.out.println(fullMessage);
  }

  public static void log(Level level, Throwable cause) {
    if (Logger.actualLevel.compareTo(level) > 0) {
      return;
    }
    cause.printStackTrace();
  }

  public static void finest(String message, Object... parameters) {
    log(Level.FINEST, Thread.currentThread(), message, parameters);
  }

  public static void fine(String message, Object... parameters) {
    log(Level.FINE, Thread.currentThread(), message, parameters);
  }

  public static void debug(String message, Object... parameters) {
    log(Level.DEBUG, Thread.currentThread(), message, parameters);
  }

  public static void debug(PreparedStatement ps) {
    String psString = ps.toString();
    int i = psString.indexOf(": ");
    String message = psString.substring(i + 2);
    log(Level.DEBUG, Thread.currentThread(), message);
  }

  public static void info(String message, Object... parameters) {
    log(Level.INFO, Thread.currentThread(), message, parameters);
  }

  public static void warning(String message, Object... parameters) {
    log(Level.WARNING, Thread.currentThread(), message, parameters);
  }

  public static void warning(Throwable e) {
    log(Level.WARNING, e);
  }

  public static void severe(Throwable e) {
    log(Level.SEVERE, e);
  }

  public static void severe(String message, Object... parameters) {
    log(Level.SEVERE, Thread.currentThread(), message, parameters);
  }

  public static void severe(String message) {
    log(Level.SEVERE, Thread.currentThread(), message);
  }

  public static void all(String message) {
    log(Level.ALL, Thread.currentThread(), message);
  }

  public static void all(String message, Object... parameters) {
    log(Level.ALL, Thread.currentThread(), message, parameters);
  }

  public static void setLevel(Level level) {
    if (actualLevel == level) {
      all("Level already on " + level + ".");
    } else {
      all("Set level to " + level + ".");
    }
    Logger.actualLevel = level;
  }

  public static Level getLevel() {
    return Logger.actualLevel;
  }
}
