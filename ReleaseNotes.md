### Nov 27, 2007 Milestone Release ###

Summary of changes:
  * Provided a HackystatUserHome class that returns the directory in which the .hackystat file should be found ([Issue 14](https://code.google.com/p/hackystat-utilities/issues/detail?id=14))

### Nov 13, 2007 Milestone Release ###

Summary of changes:
  * Provided a utility class to send Restlet Logging to a file ([Issue 13](https://code.google.com/p/hackystat-utilities/issues/detail?id=13)).

### October 16, 2007 Milestone Release ###

Summary of changes:
  * UriCache improvements (disk-based backing store, cache statistics) ([Issue 10](https://code.google.com/p/hackystat-utilities/issues/detail?id=10))
  * time.lib.jar provides time period and time interval classes. ([Issue 11](https://code.google.com/p/hackystat-utilities/issues/detail?id=11))

### October 2, 2007 Milestone Release ###

This release provides improved UriCache utility package:

Summary of changes:
  * The UriCache is completely rewritten.
  * It provides persistence and it is hooked to the Java shutdown call.
  * The underlying caching mechanism now is [org.apache.jcs.auxiliary.disk.indexed.IndexedDiskCache](http://jakarta.apache.org/jcs/apidocs/org/apache/jcs/auxiliary/disk/indexed/package-summary.html), which is different from previous  LRU Memory cacahe + Auxiliary DiskCache mechanism. In this implementation all keys of cached items reside in the memory along with disk indexes while items itself stored on the hard drive.
  * Shrinker thread is fully functional now.
  * Improved test coverage.
> Known bug: UriCache wouldn't throw the exception about instantiating a cache with the same name as the active cache, you'll get JCS exception instead.



### September 18, 2007 Milestone Release ###

This release provides improved UriCache utility package:

Summary of changes:
  * UriCache got generics now: it must be created as UriCache<K, V>.
  * UriCache is now a memory cache backed by the indexed disk cache.

Known bugs:
  * Persistence of the disk stored items doesn't work.
  * Shrinker thread is slow and sometimes seems to doesn't work at all.


### July 10, 2007 Milestone Release ###

This release provides two utility packages: hackystatlogger.lib.jar and stacktrace.lib.jar.

Package:
  * [hackystat-utilities-8.0.710](http://hackystat-utilities.googlecode.com/files/hackystat-utilities-8.0.710.zip)

Summary of changes:
  * hackystatlogger.lib.jar provides a wrapper around the Java logging package with formatters that produce timestamps in the standard hackystat fashion.
  * stacktrace.lib.jar provides a convenience class for converting a stacktrace to a string.

