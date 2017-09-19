package com.richard.mccarthy;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class CrawlerMain {

    // Site map to be updated as pages are crawled
    private ConcurrentHashMap<String, Page> siteMap = new ConcurrentHashMap<>();

    // Thread pool for getting pages and parsing them
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    public static void main (String [] args) throws IOException {

        if (args.length != 1) {
            System.out.println ("Supply the website to crawl as a parameter.");
        }

        CrawlerMain crw = new CrawlerMain();

        try {

            String domain = args[0];
            if (domain.endsWith("/")) {
                domain = domain.substring(0, domain.lastIndexOf(-1));
            }

            crw.crawlPageAndWait("/", domain);

            crw.displayPageData();

        } finally {
            // close the thread pool
            crw.executorService.shutdownNow();
        }
    }

    public void crawlPageAndWait(String crawlLink, String domain) {

        crawlPage(crawlLink, domain);

        boolean finished = false;
        int siteMapSize = 0;

        /**
         * Room for re-factoring, not a good way to wait for the end, should collect futures and check all completed
         */
        try {
            while (!finished) {
                Thread.sleep(5000);
                if (siteMapSize == siteMap.size()) {
                    return;
                } else {
                    siteMapSize = siteMap.size();
                }
            }
        }
        catch (InterruptedException | CancellationException e) {
            //@todo clean up
        }
    }

    public void crawlPage(String crawlLink, String domain) {
        String localUrl = crawlLink;
        if (!crawlLink.contains("http")) {
            localUrl = domain + crawlLink;
        }

        // Create the first page instance and put into sitemap, as itst he firsy no need to check if already present
        Page page = new Page(localUrl, domain);
        siteMap.put(page.getName(), page);
        System.out.println("Going to crawl " + localUrl);

        // Setup a callable instance to operate in a separate thread for getting the url contents and parsing it
        PageCrawlerCallable pcc = PageCrawlerCallable.createCallable(page, siteMap, executorService);
        executorService.submit(pcc);
    }

    public void displayPageData () {

        // Data in site map is unordered, so get keys in natural order
        Set<String> keySet = new HashSet<String>(siteMap.keySet());

        // Get ordered Stream of site pages
        Stream<Page> pageStream = keySet.stream().map(k -> siteMap.get(k));

        // Format each page into a String representation - Could inject different formatters
        Stream<String> formattedStream = pageStream.map(BasicPageFormatter::format);

        // Crude display using System.out::println - Could inject different displays
        formattedStream.forEach(BasicPageDisplay::display);
    }
}
