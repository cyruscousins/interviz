The Echo Nest Java API
----------------------
This is a Java client for the Echo Nest Java API.  To use the API 
get a developer key from developer.echonest.com.

    http://developer.echonest.com/

License:

    New BSD License - http://www.opensource.org/licenses/bsd-license.php


Quick Start
-------------
Here's some sample code that shows how to get similar artists
with the API:

  // Create the EchoNest class
  echoNest = new EchoNest("Your API Key");

  // Search for weezer
  List<Artist> artists = echoNest.searchArtist("weezer", false);

  // Find similar artists

  if (artists.length() > 0) {
      List<Scored<Artist>> artists = echoNest.getSimilarArtists(artist, 0, 5);
      for (Scored<Artist> sartist : artists) {
          System.out.println(sartist.getItem().getName());
      }
  }


See the 'examples' package for more code samples. Note that the jar file is
built with Java 1.6.


The Echo Nest Shell
------------------
Included with the API is a command line interface to the Echo Nest API. You can use this shell to interact with the Echo Nest.  To run the shell invoke 
the 'enshell' script, or run the command:

java -DECHO_NEST_API_KEY=$MY_ECHO_NEST_KEY -jar EchoNestAPI.jar 

Type 'help' to show the list of availabe commands.  Try some:

    nest%  get_similar weezer
    nest%  get_fam led zeppelin
    nest%  trackUpload song.mp3
    nest%  trackDuration


Revision History
----------------

Version 1.3  - November 18, 2009
    - added get_images call
    - added get_biographies call
    - increased return results for artist get_similar
    - increased return results for get_top_hot_artists

Version 1.1  - June 24, 2009
    - fixed bug where cache would grow without limits
    - added 'setMaxCacheSize' method to artist and track APIs
    - added 'setMiniumumCommandPeriod' method to artist and track APIs
    - removed extraneous test code

Version 1.0  - April 7, 2009
    - First release
    - Some minor refactorings to make javadocs better
    - javadoc improvements

Version .2  - April 6, 2009
    - added support for the track-level methods
    - improved the documentation
    - added some examples
    
Version .1  - March 27, 2009
    Initial Release
