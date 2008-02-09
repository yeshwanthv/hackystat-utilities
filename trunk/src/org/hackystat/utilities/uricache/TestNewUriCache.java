package org.hackystat.utilities.uricache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests the new version of the UriCache class.
 * 
 * @author Philip Johnson
 * 
 */
public class TestNewUriCache {
  
  private static final String testSubDir = "TestUriCache";

  /**
   * Test simple cache put and get.
   */
  @Test
  public void testSimpleCache() {
    // Create a cache
    String key = "key";
    String value = "value";
    NewUriCache cache = new NewUriCache("TestSimpleCache", testSubDir);
    cache.put(key, value);
    assertEquals("Checking simple get", value, cache.get(key));
    cache.remove(key);
    assertNull("Checking non-existant get", cache.get(key));
  }
  
  /**
   * Test simple cache put and get.
   */
  @Test
  public void testDisposeCache() {
    // Create a cache
    String key = "key";
    String value = "value";
    String cacheName = "TestDisposeCache";
    NewUriCache cache = new NewUriCache(cacheName, testSubDir);
    cache.put(key, value);
    assertEquals("Checking simple get", value, cache.get(key));
    // now dispose and try again.
    NewUriCache.dispose(cacheName);
    cache = new NewUriCache(cacheName, testSubDir);
    cache.put(key, value);
    assertEquals("Checking simple get 2", value, cache.get(key));
  }

  /**
   * Test use of disk cache.
   */
  @Test
  public void testDiskCache() {
    // Create a cache
    Long idleTime = 100L;
    Long capacity = 100L;
    NewUriCache cache = new NewUriCache("TestDiskCache", testSubDir, idleTime, capacity);
    // Now do a loop and put more than 100 items in it, forcing disk usage.
    for (int i = 1; i < 200; i++) {
      cache.put(i, i);
    }
    // Now check to see that we can retrieve all 200 items.
    for (int i = 1; i < 200; i++) {
      assertEquals("Checking retrieval", i, cache.get(i));
    }
  }
  
  /**
   * Test that we can expire elements from the cache. 
   * @throws Exception If problems occur.
   */
  @Test
  public void testElementExpiration() throws Exception {
    // Create a cache with maxLife of 1 second and a maximum of 100 in-memory elements.
    Long maxLife = 1L;
    Long capacity = 100L;
    NewUriCache cache = new NewUriCache("TestExpiration", testSubDir, maxLife, capacity);
    // Now do a loop and put 200 items in it, forcing disk usage.
    for (int i = 1; i < 200; i++) {
      cache.put(i, i);
    }
    // Add an element that expires in 3 seconds. 
    cache.put(300, 300, 3);
    // Now wait for two seconds.
    Thread.sleep(2000);
    // Now check to see that all of the items with the default maxLife are gone.
    for (int i = 1; i < 200; i++) {
      assertNull("Checking retrieval", cache.get(i));
    }
    // And check that our item with the custom maxLife time is still there.
    assertEquals("Check non-expired element", 300, cache.get(300));
    // Now wait one more second, enough time for our custom maxLife time to be exceeded.
    Thread.sleep(1001);
    // Now see that our element with the custom maxLife time is now gone.
    assertNull("Check expired element", cache.get(300));
  }
}
