package org.hackystat.utilities.time.period;


/**
 * Tag interface for <code>Day</code>, <code>Week</code>, <code>Month</code> objects.
 * 
 * @author (Cedric) Qin Zhang
 */
public interface TimePeriod extends Comparable<Object> {
  
  /**
   * Returns the first day in this TimePeriod. 
   * @return The first day associated with this time period. 
   */
  public Day getFirstDay();
}
