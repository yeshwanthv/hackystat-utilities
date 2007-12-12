package org.hackystat.utilities.tstamp;

import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.hackystat.utilities.time.period.Day;

/**
 * Utility class that facilitates Timestamp representation and processing. There
 * are too many classes already named "Timestamp", thus the abbreviated name.
 * @author Philip Johnson
 */
public class Tstamp {

  private static final String factoryErrorMsg = "Bad DataTypeFactory";
  

  /**
   * Returns true if the passed string can be parsed into an
   * XMLGregorianCalendar object.
   * @param lexicalRepresentation The string representation.
   * @return True if the string is a legal XMLGregorianCalendar.
   */
  public static boolean isTimestamp(String lexicalRepresentation) {
    try {
      DatatypeFactory factory = DatatypeFactory.newInstance();
      factory.newXMLGregorianCalendar(lexicalRepresentation);
      return true;

    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Returns an XMLGregorianCalendar, given its string representation.
   * @param lexicalRepresentation The string representation.
   * @return The timestamp.
   * @throws Exception If the string cannot be parsed into a timestamp.
   */
  public static XMLGregorianCalendar makeTimestamp(String lexicalRepresentation)
      throws Exception {
    DatatypeFactory factory = DatatypeFactory.newInstance();
    return factory.newXMLGregorianCalendar(lexicalRepresentation);
  }

  /**
   * Converts a javax.sql.Timestamp into a
   * javax.xml.datatype.XMLGregorianCalendar.
   * @param tstamp The javax.sql.Timestamp
   * @return A new instance of a javax.xml.datatype.XmlGregorianCalendar
   */
  public static XMLGregorianCalendar makeTimestamp(java.sql.Timestamp tstamp) {
    DatatypeFactory factory = null;
    try {
      factory = DatatypeFactory.newInstance();
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTimeInMillis(tstamp.getTime());
      return factory.newXMLGregorianCalendar(calendar);
    }
    catch (DatatypeConfigurationException e) {
      throw new RuntimeException(factoryErrorMsg, e);
    }
  }

  /**
   * Converts the specified time in milliseconds into a
   * javax.xml.datatype.XMLGregorianCalendar.
   * @param timeInMillis the specified time in milliseconds to convert.
   * @return A new instance of a javax.xml.datatype.XmlGregorianCalendar
   */
  public static XMLGregorianCalendar makeTimestamp(long timeInMillis) {
    DatatypeFactory factory = null;
    try {
      factory = DatatypeFactory.newInstance();
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTimeInMillis(timeInMillis);
      return factory.newXMLGregorianCalendar(calendar);
    }
    catch (DatatypeConfigurationException e) {
      throw new RuntimeException(factoryErrorMsg, e);
    }
  }
  
  /**
   * Converts the specified Day into a javax.xml.datatype.XMLGregorianCalendar.
   * @param day The day to be converted.
   * @return A new instance of a javax.xml.datatype.XmlGregorianCalendar.
   */
  public static XMLGregorianCalendar makeTimestamp(Day day) {
    DatatypeFactory factory = null;
    try {
      factory = DatatypeFactory.newInstance();
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTimeInMillis(day.getDate().getTime());
      return factory.newXMLGregorianCalendar(calendar);
    }
    catch (DatatypeConfigurationException e) {
      throw new RuntimeException(factoryErrorMsg, e);
    }
  }

  /**
   * Returns a new XMLGregorianCalendar corresponding to the passed tstamp
   * incremented by the number of days.
   * @param tstamp The base date and time.
   * @param days The number of days to increment. This can be a negative number.
   * @return A new XMLGregorianCalendar instance representing the inc'd time.
   */
  public static XMLGregorianCalendar incrementDays(XMLGregorianCalendar tstamp, int days) {
    DatatypeFactory factory = null;
    try {
      factory = DatatypeFactory.newInstance();
      GregorianCalendar calendar = new GregorianCalendar();
      long millis = tstamp.toGregorianCalendar().getTimeInMillis();
      millis += 1000L * 60 * 60 * 24 * days;
      calendar.setTimeInMillis(millis);
      return factory.newXMLGregorianCalendar(calendar);
    }
    catch (DatatypeConfigurationException e) {
      throw new RuntimeException(factoryErrorMsg, e);
    }
  }

  /**
   * Returns a new XMLGregorianCalendar corresponding to the passed tstamp
   * incremented by the number of hours.
   * @param tstamp The base date and time.
   * @param hours The number of hours to increment. This can be a negative
   * number.
   * @return A new XMLGregorianCalendar instance representing the inc'd time.
   */
  public static XMLGregorianCalendar incrementHours(XMLGregorianCalendar tstamp, int hours) {
    DatatypeFactory factory = null;
    try {
      factory = DatatypeFactory.newInstance();
      GregorianCalendar calendar = new GregorianCalendar();
      long millis = tstamp.toGregorianCalendar().getTimeInMillis();
      millis += 1000L * 60 * 60 * hours;
      calendar.setTimeInMillis(millis);
      return factory.newXMLGregorianCalendar(calendar);
    }
    catch (DatatypeConfigurationException e) {
      throw new RuntimeException(factoryErrorMsg, e);
    }
  }

  /**
   * Returns a new XMLGregorianCalendar corresponding to the passed tstamp
   * incremented by the number of minutes.
   * @param tstamp The base date and time.
   * @param minutes The number of minutes to increment. This can be a negative
   * number.
   * @return A new XMLGregorianCalendar instance representing the inc'd time.
   */
  public static XMLGregorianCalendar incrementMinutes(XMLGregorianCalendar tstamp, int minutes) {
    DatatypeFactory factory = null;
    try {
      factory = DatatypeFactory.newInstance();
      GregorianCalendar calendar = new GregorianCalendar();
      long millis = tstamp.toGregorianCalendar().getTimeInMillis();
      millis += 1000L * 60 * minutes;
      calendar.setTimeInMillis(millis);
      return factory.newXMLGregorianCalendar(calendar);
    }
    catch (DatatypeConfigurationException e) {
      throw new RuntimeException(factoryErrorMsg, e);
    }
  }

  /**
   * Returns a new XMLGregorianCalendar corresponding to the passed tstamp
   * incremented by the number of seconds.
   * @param tstamp The base date and time.
   * @param seconds The number of seconds to increment. This can be a negative
   * number.
   * @return A new XMLGregorianCalendar instance representing the inc'd time.
   */
  public static XMLGregorianCalendar incrementSeconds(XMLGregorianCalendar tstamp, int seconds) {
    DatatypeFactory factory = null;
    try {
      factory = DatatypeFactory.newInstance();
      GregorianCalendar calendar = new GregorianCalendar();
      long millis = tstamp.toGregorianCalendar().getTimeInMillis();
      millis += 1000L * seconds;
      calendar.setTimeInMillis(millis);
      return factory.newXMLGregorianCalendar(calendar);
    }
    catch (DatatypeConfigurationException e) {
      throw new RuntimeException(factoryErrorMsg, e);
    }
  }

  /**
   * Returns a new java.sql.Timestamp created from a
   * javax.xml.datatype.XMLGregorianCalendar.
   * @param calendar The XML timestamp.
   * @return The SQL timestamp.
   */
  public static java.sql.Timestamp makeTimestamp(XMLGregorianCalendar calendar) {
    return new java.sql.Timestamp(calendar.toGregorianCalendar().getTimeInMillis());
  }

  /**
   * Returns an XMLGregorianCalendar corresponding to the current time.
   * @return The timestamp.
   */
  public static XMLGregorianCalendar makeTimestamp() {
    try {
      DatatypeFactory factory = DatatypeFactory.newInstance();
      return factory.newXMLGregorianCalendar(new GregorianCalendar());
    }
    catch (Exception e) {
      throw new RuntimeException(factoryErrorMsg, e);
    }
  }

  /**
   * Returns an XMLGregorianCalendar corresponding to 01-Jan-2000.
   * @return The timestamp.
   */
  public static XMLGregorianCalendar getDefaultProjectStartTime() {
    try {
      DatatypeFactory factory = DatatypeFactory.newInstance();
      XMLGregorianCalendar startTime = factory.newXMLGregorianCalendar();
      startTime.setDay(1);
      startTime.setMonth(1);
      startTime.setYear(2000);
      startTime.setTime(0, 0, 0);
      startTime.setMillisecond(000); // NOPMD
      return startTime;
    }
    catch (Exception e) {
      throw new RuntimeException(factoryErrorMsg, e);
    }
  }

  /**
   * Returns an XMLGregorianCalendar corresponding to 01-Jan-2010.
   * @return The timestamp.
   */
  public static XMLGregorianCalendar getDefaultProjectEndTime() {
    try {
      DatatypeFactory factory = DatatypeFactory.newInstance();
      XMLGregorianCalendar endTime = factory.newXMLGregorianCalendar();
      endTime.setDay(1);
      endTime.setMonth(1);
      endTime.setYear(2010);
      endTime.setTime(23, 59, 59);
      endTime.setMillisecond(999);
      return endTime;
    }
    catch (Exception e) {
      throw new RuntimeException(factoryErrorMsg, e);
    }
  }
  
  /**
   * In the early days of Hackystat, default project start times were 1000-01-01.
   * This was stupid.  The following hack exists to correct projects containing this old 
   * value.  This code and its callers can be removed when the disease is eradicated.
   * @param startTime The startTime in question.
   * @return True if it's before 1950.
   */
  public static boolean isBogusStartTime(XMLGregorianCalendar startTime) {
    try {
      XMLGregorianCalendar bogusTime = Tstamp.makeTimestamp("1950-01-01");
      return Tstamp.lessThan(startTime, bogusTime);
    }
    catch (Exception e) {
      return true;
    }
  }

  /**
   * Returns true if tstamp is equal to or between start and end.
   * @param start The start time.
   * @param tstamp The timestamp to test.
   * @param end The end time.
   * @return True if tstamp is between start and end.
   */
  public static boolean inBetween(XMLGregorianCalendar start, XMLGregorianCalendar tstamp,
      XMLGregorianCalendar end) {
    if ((Tstamp.equal(start, tstamp)) || (Tstamp.equal(end, tstamp))) {
      return true;
    }
    if ((start.compare(tstamp) == DatatypeConstants.LESSER)
        && (end.compare(tstamp) == DatatypeConstants.GREATER)) {
      return true;
    }
    return false;
  }

  /**
   * Returns true if time1 > time2.
   * @param time1 The first time.
   * @param time2 The second time.
   * @return True if time1 > time2
   */
  public static boolean greaterThan(XMLGregorianCalendar time1, XMLGregorianCalendar time2) {
    return time1.compare(time2) == DatatypeConstants.GREATER;
  }

  /**
   * Returns true if timeString1 > timeString2. Throws an unchecked
   * IllegalArgument exception if the strings can't be converted to timestamps.
   * @param timeString1 The first time.
   * @param timeString2 The second time.
   * @return True if time1 > time2
   */
  public static boolean greaterThan(String timeString1, String timeString2) {
    try {
      DatatypeFactory factory = DatatypeFactory.newInstance();
      XMLGregorianCalendar time1 = factory.newXMLGregorianCalendar(timeString1);
      XMLGregorianCalendar time2 = factory.newXMLGregorianCalendar(timeString2);
      return time1.compare(time2) == DatatypeConstants.GREATER;
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Illegal timestring", e);
    }
  }

  /**
   * Returns true if time1 < time2.
   * @param time1 The first time.
   * @param time2 The second time.
   * @return True if time1 < time2
   */
  public static boolean lessThan(XMLGregorianCalendar time1, XMLGregorianCalendar time2) {
    return time1.compare(time2) == DatatypeConstants.LESSER;
  }

  /**
   * Returns true if time1 equals time2. Note that this class compares for
   * equality by converting to millis, which is apparently not what the built-in
   * XMLGregorianCalendar class does, which leads to the XMLGregorianCalendar
   * class saying that two logically equal instances are not equal. For example,
   * 2007-01-01 does not equal 2007-01-01T00:00:00 using
   * XMLGregorianCalendar.equals(). The Tstamp.equal() method does the right
   * thing in this case.
   * @param time1 The first time.
   * @param time2 The second time.
   * @return True if time1 equals time2
   */
  public static boolean equal(XMLGregorianCalendar time1, XMLGregorianCalendar time2) {
    long millis1 = time1.toGregorianCalendar().getTimeInMillis();
    long millis2 = time2.toGregorianCalendar().getTimeInMillis();
    return millis1 == millis2;
  }

}
