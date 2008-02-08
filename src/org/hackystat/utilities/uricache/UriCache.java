package org.hackystat.utilities.uricache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.jcs.auxiliary.disk.indexed.IndexedDiskCache;
import org.apache.jcs.auxiliary.disk.indexed.IndexedDiskCacheAttributes;
import org.apache.jcs.engine.CacheElement;
import org.apache.jcs.engine.ElementAttributes;
import org.apache.jcs.engine.behavior.ICacheElement;
import org.apache.jcs.engine.behavior.IElementAttributes;
import org.apache.jcs.engine.stats.behavior.IStatElement;
import org.apache.jcs.engine.stats.behavior.IStats;

/**
 * Provides a caching mechanism based upon Apache JCS (Java Caching System). 
 * 
 * @author <a href="mailto:seninp@gmail.com">Pavel Senin<a>
 */
public class UriCache {

  /** JCS cache handler. */
  private IndexedDiskCache uriCache;

  /** JCS cache name. */
  private String cacheName;

  /** JCS cache properties set. */
  private Properties cacheProperties;

  /** JCS element "life time". */
  private Long maxIdleTime;

  /** JCS instances name holder */
  private static ArrayList<String> cacheNames = new ArrayList<String>();

  /**
   * Constructor, an instance of UriCache configured according to the Properties provided.
   * 
   * @param cacheName the name used for this cache identification and to name the 
   * file containing this cache.
   * @param cacheProperties the cache configuration properties.
   * @throws UriCacheException If the cacheName is already in use.
   */
  public UriCache(String cacheName, UriCacheProperties cacheProperties) throws UriCacheException {

    // getting rid of DEBUG level log messages
    Level loggerLevel = cacheProperties.getLoggerLevel();
    Logger logger = Logger.getLogger("org.apache.jcs");
    logger.setLevel(loggerLevel);

    this.cacheName = cacheName;

    // check for the name duplication in here
    if (UriCache.cacheNames.contains(this.cacheName)) {
      throw new UriCacheException(
          "Error while attemting get a cache instance: the cache region name " + cacheName
              + " is in use.");
    }
    else {
      UriCache.cacheNames.add(this.cacheName);
    }

    this.cacheProperties = cacheProperties.getProperties();
    this.maxIdleTime = Long.valueOf(this.cacheProperties
        .getProperty("jcs.region.UriCache.elementattributes.MaxLifeSeconds"));

    IndexedDiskCacheAttributes cattr = new IndexedDiskCacheAttributes();
    cattr.setCacheName(this.cacheName);
    cattr.setMaxKeySize(Integer.valueOf(this.cacheProperties
        .getProperty("jcs.region.UriCache.cacheattributes.MaxObjects")));
    cattr.setDiskPath(cacheProperties.getCacheStoragePath());
    this.uriCache = new IndexedDiskCache(cattr);
  }

  /**
   * Place a new item in the cache, associated with uriString. If there is currently an item
   * associated with the same uriString it is replaced.
   * 
   * @param uriString identity (URI) of the object to cache.
   * @param obj The object to cache.
   * @throws UriCacheException in case of error.
   */
  public void cache(Serializable uriString, Serializable obj) throws UriCacheException {
    //
    // I'm hardcoding parameters here, probably good move is to get these from properties when
    // everything will be working.
    //
    IElementAttributes eAttr = new ElementAttributes();
    eAttr.setIdleTime(this.maxIdleTime);
    eAttr.setIsSpool(true);
    eAttr.setIsEternal(false);
    eAttr.setIsLateral(false);
    eAttr.setIsRemote(false);
    eAttr.setIsSpool(true);
    ICacheElement element = new CacheElement(this.cacheName, uriString, obj);
    element.setElementAttributes(eAttr);
    this.uriCache.doUpdate(element);
  }

  /**
   * Puts object into cache and specifies object lifetime within the cache.
   * 
   * @param uriString Identity of the object to cache.
   * @param obj Object to cache.
   * @param expirationTimeStamp Object expiration timestamp, object will be removed from the cache
   *        once System time is greater than the timestamp value.
   * @throws UriCacheException in case of error.
   */
  public void cache(Serializable uriString, Serializable obj,
      XMLGregorianCalendar expirationTimeStamp) throws UriCacheException {

    // first of all calculating the life time of this object from now
    Long currTime = System.currentTimeMillis();
    Long lifeTime = ((Integer) expirationTimeStamp.getMillisecond()).longValue() - currTime;

    IElementAttributes eAttr = new ElementAttributes();
    eAttr.setIdleTime(lifeTime);
    eAttr.setIsSpool(true);
    eAttr.setIsEternal(false);
    eAttr.setIsLateral(false);
    eAttr.setIsRemote(false);
    eAttr.setIsSpool(true);
    ICacheElement element = new CacheElement(this.cacheName, uriString, obj);
    element.setElementAttributes(eAttr);
    this.uriCache.doUpdate(element);
  }

  /**
   * Removes all elements from the cache.
   * 
   * @throws UriCacheException in case of error.
   */
  public void clear() throws UriCacheException {
    this.uriCache.doRemoveAll();
  }

  /**
   * ShutDowns the cache, flushes all items from the memory to cache and clears memory region.
   * 
   */
  public void shutdown() {
    this.uriCache.dispose();
    UriCache.cacheNames.remove(this.cacheName);
  }

  /**
   * Retrieves object from the cache.
   * 
   * @param uriString URI of the object to search for.
   * @return The cached object or <em>null</em> if not found.
   */
  public Serializable lookup(Serializable uriString) {
    ICacheElement ce = this.uriCache.get(uriString);
    if (null == ce) {
      return null;
    }
    else {
      return ce.getVal();
    }
  }

  /**
   * Removes an Object from the cache.
   * 
   * @param uriString Identity of the object to be removed.
   */
  public void remove(Serializable uriString)  {
    this.uriCache.remove(uriString);
  }

  /**
   * Sets the cache auto-expire time. Cache will auto expire elements after specified seconds to
   * reclaim space.
   * 
   * @param seconds The new ShrinkerIntervalSeconds value.
   */
  public void setMaxMemoryIdleTimeSeconds(Long seconds) {
    this.maxIdleTime = seconds;
  }

  /**
   * Reports the cache memory region name.
   * 
   * @return cache name.
   */
  public String getName() {
    return this.cacheName;
  }

  /**
   * Reports cache statistics:
   * <ul>
   * <li>Cache type</li>
   * <li>Is Alive</li>
   * <li>Key Map Size</li>
   * <li>Data File Length</li>
   * <li>Hit Count</li>
   * <li>Bytes Free</li>
   * <li>Optimize Operation Count</li>
   * <li>Times Optimized</li>
   * <li>Recycle Count</li>
   * <li>Recycle Bin Size</li>
   * <li>Startup Size</li>
   * </ul>
   * 
   * @return cache statistics.
   */
  public Map<String, String> getStatistics() {
    IStats st = this.uriCache.getStatistics();
    IStatElement[] elements = st.getStatElements();
    Map<String, String> res = new HashMap<String, String>();
    res.put("Cache type", st.getTypeName());
    for (IStatElement e : elements) {
      res.put(e.getName(), e.getData());
    }
    return res;
  }

}
