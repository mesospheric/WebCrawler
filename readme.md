#Web Crawler

##Build and Run Instructions

###Build

Make sure that Maven 3 is installed and configured then on the command line from the top folder of the project run 'mvn clean install'. This will create a file called 'WebCrawler-1.0-SNAPSHOT.jar-with-dependencies.jar' in 'target' folder.
 
###Run
On the command line from the top folder of the project type 'java -jar ./target/WebCrawler-1.0-SNAPSHOT-jar-with-dependencies.jar https://pingdom.com'


##Threading
My crawler uses a thread pool for retrieving and parsing the page contents. I felt this would speed up performance as there would always be multiple pages to work on. 

The way that the crawler detects that that all crawling has ceased is not production ready, but I ran out of time. A much better solution would be to utilise the Futures returned by the task submission and then track that all of these have completed. 

I assumed a thread pool size but more testing would be required to get a correct value.

I used Stream handling for parsing the content of a web page but intentionally did not use paralell streams. This was due to the fact that my data structures were Sets which are not thread safe. Sets gave me the benefit of ordering and handling uniqueness and I weighed that off aganist using a different concurrent collection and then having to sort and clean duplicates and i felt right now it was not worth going parallel without spending time performance testing which was beyond the scope of this exercise.


##JSoup
I used JSoup to retrieve and parse the HTML files. This proved to be quick to develop but required a bit more effort with the Unit testing. One potential drawback of this choice is the handling of the connections may not be as efficient as the HttpURLConnection which can keep alive underlying connections for requests to the same site.

##Formatting of Output and Display
I created a simple formatting implementation class that could easily be substituted for something better and used the same technique with the display. It would be useful to inject different formatters and 'display' implementations.

## Known Issues

1. When parsing a domain like manning.com a link that was returned of the form accounts.manning.com is not be detected as belonging to the same domain.
2. When parsing a domain like manning.com a link that was returned of the form www.manning.com is not be detected as belonging to the same domain.
3. The crawler would benefit from a configuration file instead of hard coding values in the program, for instance, thread pool max size
4. There is no use of logging frameworks in the code, this would be simple to implement
5. Some high level acceptance tests that parsed data a test site made from local html files would be useful addition.

