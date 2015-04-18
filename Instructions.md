This will help you take in swagswap quickly:

# Prerequisites #
  * [Get GAE/J](http://code.google.com/appengine/downloads.html) (latest version)
  * [Get GWT](http://code.google.com/webtoolkit/versions.html) (latest 1.7 version)

# Installing and Running #

  * When you [check out swagswap in eclipse](http://code.google.com/p/swagswap/source/checkout), eclipse the project settings should be recognized.
  * In eclipse go to project properties, Libraries, Add Variable, Configure Variables, New and add
    * GAE\_HOME with the value pointing to your appengine (1.2.x) installation
    * GWT\_HOME with the value pointing to your GWT 1.7.x installation
      * Note: For Mac OS X 10.6.2 (Snow Leopard) follow [this additional step](OSX62ExtraStep.md)
  * You'll also have to set an environment variable on your system called `GAE_HOME` and `GWT_HOME` pointing to your installations and restart eclipse.
  * You might have to adjust the referenced JDK (point to JDK6).
  * Configure ant task to run in a separate JVM. Right click on the task, Select 'Run As' then select 'External Tools Configuration...'.  Click on the JRE tab.  Select the 'Separate JRE' option and choose your JVM.  Hit 'Apply'.
  * Start the app with
    * `ant server_run` (and see the app at http://localhost:8080),
    * `ant server_stop(windows)` to stop it on windows.  On Mac you can kill the process by clicking the 'stop' button on the console.
    * `ant server_debug` to run the server in debug mode so you can attach remotely in eclipse.


# Running the Tests #

  * Run the unit tests with `ant runtests`.
  * You can run (or debug!) the tests from eclipse (right click on a test and Run as JUnit test) but you have to make sure you run ant compile-tests first (and every time you make any code changes).


# Brief intro to the code #

  * The best place to start is in [ItemController](http://code.google.com/p/swagswap/source/browse/trunk/src/main/java/com/swagswap/web/springmvc/controller/ItemController.java).  That is the RESTful Spring MVC annotated controller which defines REST endpoints (urls) and does the controlling with a minimum of code and no config file.

  * The ItemController uses [ItemServiceImpl](http://code.google.com/p/swagswap/source/browse/trunk/src/main/java/com/swagswap/service/ItemServiceImpl.java) which for the most part simply delegates to ItemDaoImpl.  One of the reasons ItemServiceImpl is there can be seen in the save() method which is marked @Transactional for the atomic insertion of Items their child Images.  This is also the layer we inject the [SwagSwapCacheManager](http://code.google.com/p/swagswap/source/browse/trunk/src/main/java/com/swagswap/dao/cache/SwagCacheManagerImpl.java) into.

  * [ItemDaoImpl](http://code.google.com/p/swagswap/source/browse/trunk/src/main/java/com/swagswap/dao/ItemDaoImpl.java) does the JDO DB stuff.

  * The other interesting thing is how images are streamed from the DB.  That can be seen in the [ImageController](http://code.google.com/p/swagswap/source/browse/trunk/src/main/java/com/swagswap/web/springmvc/controller/ImageController.java) in the streamImageContent() method.

  * It is called from JSP tags that trick the browser into thinking the dynamic calls are HTML img tags.  Look on [listSwagItems.jsp](http://code.google.com/p/swagswap/source/browse/trunk/src/main/webapp/WEB-INF/jsp/listSwagItems.jsp) and search for showImage to see it called.


# Reporting Bugs and Issue Tracking #

  * Bug reports are welcome at http://code.google.com/p/swagswap/issues/list.

  * If you didn't already know about the [Google Code Issue Tracker and Subversion comment integration](http://code.google.com/p/support/wiki/IssueTracker#Integration_with_version_control)

  * Check out the links to the diffs on two commits in, for example [issue 10](http://code.google.com/p/swagswap/issues/detail?id=10&can=1):



Enjoy.