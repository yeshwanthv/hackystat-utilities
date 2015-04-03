# Introduction #

Each time you build the Hackystat system from sources, you "publish" the jar files and other exportable artifacts associated with each Hackystat project to an Ivy "repository" on your local file system. This repository is located in ~/.ivy2/local-repository/org.hackystat.

The structure of this repository directory is as follows:

```
org.hackystat/
              hackystat-utilities/
                                 2009.07.20.14.54.37/
                                                     (many files)
                                 
                                 2009.07.20.13.12.34/
                                                     (many files)
                                 :
              hackystat-sensorbase-uh/
                                      2009.07.20.14.54.37/
                                                          (many files) 
                                      :
              :
```

Since the Hackystat build system always references the "latest.integration", all but the most recent published set of files are extraneous.  Furthermore, an active development process can easily generate hundreds or thousands of these outdated publications in a relatively short period of time. For example, I recently had 1400 files in my local-repository directory, occupying 330MB of disk space.

To simply the maintenance of this directory, the hackystat-utilities package contains a class called CleanLocalRepository. This class will delete all publications directories from each Hackystat module except for the most recent one.  There is an associated Ant target in build.xml called "cleanlocalrepository" that invokes this class.

Thus, to garbage collect the local repository, you can cd to the hackystat-utilities directory and run "ant cleanlocaldirectory".  The class prints out messages as it runs about the files it is deleting. If there is only one published version of a module, it will not delete it.

Here is a sample output:

```
C:\hackystat-projects\hackystat-utilities>ant cleanlocalrepository
Buildfile: build.xml

download-ivy-if-necessary:

install-ivy:

install-libraries:
No ivy:settings found for the default reference 'ivy.instance'.  A default instance will be used
[ivy:retrieve] :: Ivy 2.1.0-rc1 - 20090319213629 :: http://ant.apache.org/ivy/ ::
:: loading settings :: file = C:\hackystat-projects\hackystat-utilities\ivysettings.xml

compile:
    [javac] Compiling 2 source files to C:\hackystat-projects\hackystat-utilities\build\classes

cleanlocalrepository:
     [java] Processing repository: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata
     [java]   Deleting contents of directory: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata\2009.07.01.10.39.19
     [java]     Deleting file: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata\2009.07.01.10.39.19\dailyprojectdata-2009.07.01.10.39.19.jarwas OK
     [java]     Deleting file: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata\2009.07.01.10.39.19\dailyprojectdata-2009.07.01.10.39.19.jar.md5 was OK
     [java]     Deleting file: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata\2009.07.01.10.39.19\dailyprojectdata-2009.07.01.10.39.19.jar.sha1 was OK
     [java]     Deleting file: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata\2009.07.01.10.39.19\dailyprojectdata.lib-2009.07.01.10.39.19.jar was OK
     [java]     Deleting file: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata\2009.07.01.10.39.19\dailyprojectdata.lib-2009.07.01.10.39.19.jar.md5 was OK
     [java]     Deleting file: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata\2009.07.01.10.39.19\dailyprojectdata.lib-2009.07.01.10.39.19.jar.sha1 was OK
     [java]     Deleting file: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata\2009.07.01.10.39.19\hackystat-analysis-dailyprojectdata-2009.07.01.10.39.19.xml was OK
     [java]     Deleting file: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata\2009.07.01.10.39.19\hackystat-analysis-dailyprojectdata-2009.07.01.10.39.19.xml.md5 was OK
     [java]     Deleting file: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata\2009.07.01.10.39.19\hackystat-analysis-dailyprojectdata-2009.07.01.10.39.19.xml.sha1 was OK

(many lines of output deleted)

BUILD SUCCESSFUL
Total time: 4 seconds
C:\hackystat-projects\hackystat-utilities>
```

Note that if we now invoke the command again, no deletions are done:

```
C:\hackystat-projects\hackystat-utilities>ant cleanlocalrepository
Buildfile: build.xml

download-ivy-if-necessary:

install-ivy:

install-libraries:
No ivy:settings found for the default reference 'ivy.instance'.  A default instance will be used
[ivy:retrieve] :: Ivy 2.1.0-rc1 - 20090319213629 :: http://ant.apache.org/ivy/ ::
:: loading settings :: file = C:\hackystat-projects\hackystat-utilities\ivysettings.xml

compile:

cleanlocalrepository:
     [java] Processing repository: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-dailyprojectdata
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-analysis-telemetry
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-developer-example
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-sensor-ant
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-sensor-shell
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-sensor-xmldata
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-sensorbase-postgres
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-sensorbase-simdata
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-sensorbase-uh
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-ui-tickertape
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-ui-wicket
     [java]   No old publications to delete.
     [java] Processing module: C:\Documents and Settings\johnson\.ivy2\local-repository\org.hackystat\hackystat-utilities
     [java]   No old publications to delete.
     [java] Finished processing the local hackystat repository.

BUILD SUCCESSFUL
Total time: 1 second
Sending build result to Hackystat server...

```

After cleaning, the local-repository contains about 170 files and occupies about 50MB of disk space.