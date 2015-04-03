# 1.0 Introduction #

This User Guide provides instructions on how to download, install,
configure, and use the Hackystat UriCache library. It also provides simple
examples of library usage.

The UriCache system is a wrapper around the
[Apache JCS](http://jakarta.apache.org/jcs/) system. It is designed to
provide an API well suited to the needs of Hackystat services.

UriCache is configured to use the JCS IndexedDiskCache auxiliary cache
using the "UPDATE" access pattern.  This means that all items added to the
cache will also be spooled to disk.

Note that both keys and values for the cached items must be Serializable.

UriCache will automatically expire items from the cache according to two algorithms:

  1. If the user specifies an expiration time when adding an item, then the item will be removed after this expiration time is reached. By default, items expire 24 hours after being added. It is possible to specify an expiration time for the each item individually.
  1. If the maximum cache capacity is reached, then items are removed according to a least recently used algorithm.

Of course, it is also possible for a client to remove any instances manually, or even clear the cache entirely.

# 2.0 Downloading #

The UriCache package is distributed as part of the Hackystat Utilities package, which is incorporated by default into all Hackystat services.  Thus, in most cases you do not need to do anything extra to download or install the UriCache; it is already present in your system.

It is also possible to download the sources from the [Hackystat-Utilities SVN repository](http://code.google.com/p/hackystat-utilities/source), but in that case you will need to build the library from sources. The distribution package comes with both sources and a pre-built libraries.

# 3.0 Prerequisites #

If you wish to build the UriCache system from sources, you will need to install the following packages, environment variables, and Eclipse classpath variables as follows:

| **Package** | **Version** | **Environment/Classpath Variable** | **Sample Env Value** |
|:------------|:------------|:-----------------------------------|:---------------------|
| [Apache Commons Logging](http://commons.apache.org/logging/) | 1.1 |APACHE\_COMMONS\_LOGGING\_HOME | c:\tools\commons-logging-1.1 |
| [Apache Java Caching System](http://jakarta.apache.org/jcs/) | 1.3 | APACHE\_JCS\_HOME | c:\tools\apache-jcs-1.3 |

UriCache also uses the util.concurrent Release 1.3.4. by [Doug Lea](http://gee.cs.oswego.edu/dl) which is included in the hackystat-utilities project in its lib/ directory.

# 4.0 Usage #

## 4.1. The UriCache constructor ##

To create an instance of a UriCache, you can use either one of the two UriCache constructors:

```
new UriCache(String cacheName, String subDir)
```

or

```
new UriCache(String cacheName, String subDir, Double maxLifeDays, Long capacity)
```

These constructors indicate the two required and two optional properties available for UriCaches:

| Property | Default | Description |
|:---------|:--------|:------------|
| cacheName | N/A | All UriCaches must be passed a unique name, which will be used to generate the disk cache file names (**cacheName**.key and **cacheName**.data). |
| subDir | N/A | Specifies the subdirectory in which the disk cache files will be stored.  This directory is created if not present. Multiple UriCaches can be stored in the same subdirectory. |
| maxLifeDays | 1 | If a cache element is not retrieved within this number of days after being added, it is removed from the cache. |
| capacity | 10000 | The maximum number of elements that the cache can hold. If this capacity is reached, then the addition of new elements results in old elements being implicitly removed from the cache (according to a least recently used strategy).|

## 4.2 Manipulating the cache ##

Here is an example method from the DailyProjectData service that shows the use of the get() and put() methods. The cache has been instantiated elsewhere and is accessable in the this.uriCache instance variable.

```
 public synchronized CouplingDailyProjectData getCoupling(String user, String project,
      XMLGregorianCalendar timestamp, String type, String tool) 
  throws DailyProjectDataClientException {
    Date startTime = new Date();
    CouplingDailyProjectData coupling;
    String uri = "coupling/" + user + "/" + project + "/" + timestamp + "/" + type + "?Tool=" 
    + tool;
    // Check the cache, and return the instance from it if available. 
    if (this.isCacheEnabled) {
      coupling = (CouplingDailyProjectData)this.uriCache.get(uri);
      if (coupling != null) {
        return coupling;
      }
    }
    Response response = makeRequest(Method.GET, uri, null);
    if (!response.getStatus().isSuccess()) {
      System.err.println("coupling/" + user + "/" + project + "/" + timestamp + "/" + type);
      throw new DailyProjectDataClientException(response.getStatus());
    }
    try {
      String xmlData = response.getEntity().getText();
      coupling = makeCouplingDailyProjectData(xmlData);
      // Add it to the cache if we're using one.
      if (this.isCacheEnabled && !isToday(timestamp)) {
        this.uriCache.put(uri, coupling);
      }
    }
    catch (Exception e) {
      logElapsedTime(uri, startTime, e);
      throw new DailyProjectDataClientException(response.getStatus(), e);
    }
    logElapsedTime(uri, startTime);
    return coupling;
  }
```

Note that we do not add entries to the cache for data from the current day.  This is because we expect that data from the current day will be changing and so we want to recompute this data from scratch each time we require it.

## 4.3 The UriCache shutdown hook ##

The UriCache installs a shutdown hook which calls dispose() on all instantiated caches when the JVM exits.  This is intended to ensure that the disk cache gets closed correctly.

## 4.4 Cache groups ##

Within a given UriCache, it is possible to create "groups", which are basically independent caches that share the same backing store but have their own "namespace".  The UriCache provides a set of "group" methods: putInGroup(), getFromGroup(), removeFromGroup, getGroupKeys(), and getGroupSize(), which all take a group name as an argument.

There is no explicit group definition method; to create a group, simply use putInGroup and supply the group name you want to use.

Note that if you use putInGroup(), you must access that instance using getFromGroup(); the get() method will not retrieve it.

You typically want to use group-based access rather than the default access if you want to obtain the set of keys associated with a subset of cached instances and don't want to have to iterate over the entire cache.

## 4.5 Concurrency issues ##

UriCache automatically provides a "write through" cache using JCS with a backing indexed disk cache.  There is a major bug/shortcoming of JCS in this respect, because if two threads access the same backing store concurrently, then erroneous results will occur: https://issues.apache.org/jira/browse/JCS-31.

To avoid this problem, care must be taken to ensure that two instances of a UriCache with the same cacheName and subdirectory are not running concurrently.

Here, for example, is the authentication code from the DailyProjectData server, which uses a synchronized block to ensure that only one instance of a SensorBaseClient for a given user will be created, regardless of how many threads are running the Authenticator instance (or instances).

```
 @Override protected boolean checkSecret(String identifier, char[] secretCharArray) {
    /*
     * I am synchronizing here on a static (class-wide) variable for two reasons:
     * (1) JCS write-through caching fails when multiple threads access the same back-end file:
     * <https://issues.apache.org/jira/browse/JCS-31>. Thus, it is vitally important to ensure
     * that only one instance of a SensorBaseClient for any given user is created.
     * (2) I do not know if Restlet allows multiple Authenticator instances.  Thus, I am 
     * synchronizing on a class-wide variable just in case. 
     * This synchronization creates a bottleneck on every request, but the benefits of reliable
     * caching should outweigh this potential performance hit under high loads. 
     */
    synchronized (userClientMap) {
      String secret = new String(secretCharArray);
      // Return true if the user/password credentials are in the cache.
      if (credentials.containsKey(identifier) && secret.equals(credentials.get(identifier))) {
        return true;
      }
      // Otherwise we check the credentials with the SensorBase.
      boolean isRegistered = SensorBaseClient.isRegistered(sensorBaseHost, identifier, secret);
      if (isRegistered) {
        // Credentials are good, so save them and create a sensorbase client for this user.
        credentials.put(identifier, secret);
        SensorBaseClient client = new SensorBaseClient(sensorBaseHost, identifier, secret);
        // Set timeout to 60 minutes.
        client.setTimeout(1000 * 60 * 60);
        // Get the ServerProperties instance so we can determine if caching is enabled.
        Server server = (Server) getContext().getAttributes().get("DailyProjectDataServer");
        ServerProperties props = server.getServerProperties();
        if (props.isCacheEnabled()) {
          client.enableCaching(identifier, "dailyprojectdata", props.getCacheMaxLife(), props
              .getCacheCapacity());
        }
        userClientMap.put(identifier, client);
      }
    return isRegistered;
    }
  }
```

Another possibility is if two identically configured instances of the same service are running on the same host at the same time.



















