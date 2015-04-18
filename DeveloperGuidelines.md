A few guidelines if you please

# Committing #
  * Tag you fixed issues with the latest yet-to-be-release version: example: 0dot3
  * Try and use [issue tracker integration with version control](http://code.google.com/p/support/wiki/IssueTracker#Integration_with_version_control)

# Deploying #
  * Make sure application-web.xml version points to a non-live version like test
  * Deploy there first and test it out.
  * To ensure we have we'll have a live running version of each deployment for reference:
    * When your ready to bring it live SVN tag a new version (like 0dot3) and deploy under that name.
    * Then switch the live version to your new version.