package com.richard.mccarthy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.concurrent.*;

public class PageCrawlerCallable implements Callable<Page> {

    private Page page;
    private ConcurrentHashMap<String, Page> siteMap;
    private ExecutorService executorService;
    private PageRetriever pageRetriever;

    private PageCrawlerCallable(Page page, ConcurrentHashMap siteMap, ExecutorService executorService, PageRetriever pageRetriever) {
        this.page = page;
        this.siteMap = siteMap;
        this.executorService = executorService;
        this.pageRetriever = pageRetriever;
    }

    public static PageCrawlerCallable createCallable(Page page, ConcurrentHashMap siteMap, ExecutorService executorService) {
        return new PageCrawlerCallable(page, siteMap, executorService, new PageRetriever());
    }

    public static PageCrawlerCallable createCallable(Page page, ConcurrentHashMap siteMap, ExecutorService executorService, PageRetriever pageRetriever) {
        return new PageCrawlerCallable(page, siteMap, executorService, pageRetriever);
    }

    @Override
    public Page call() throws Exception {
        System.out.println("Crawling " + page.getDomain() + " " + page.getName());
        //Document doc = Jsoup.connect(page.getName()).get();
        Document doc = pageRetriever.getDocument(page.getName());
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        System.out.println("Finished crawling of " + page.getDomain() + " " + page.getName());

        extractItems(page, links, media, imports);

        crawlLinks();
        return page;
    }

    private Page extractItems (final Page page, final Elements links, final Elements media, final Elements imports) {

        links.stream().map(l -> l.attr("href")).forEach(page);

        // Add the media and imports into static content set. Will include internal and external static content
        media.stream().forEach(m -> page.getStaticContent().add(m.attr("src")));
        imports.stream().forEach(i -> page.getStaticContent().add(i.attr("href")));

        return page;
    }

    private void crawlLinks() {

        for (String link : page.getLinks()) {
            String localUrl = link;
            if(!link.contains("http"))
            {
                localUrl = page.getDomain() + link;
            }

            Page newPage = new Page(localUrl, page.getDomain());

            // Only crawl if not a link into same page and not yet visited
            if(!localUrl.contains("#") && siteMap.putIfAbsent(newPage.getName(),newPage)==null)
            {
                System.out.println("Going to crawl " + localUrl);

                // Setup a callable instance to operate in a separate thread for getting the url contents and parsing it
                PageCrawlerCallable pcc = PageCrawlerCallable.createCallable(newPage, siteMap, executorService);

                executorService.submit(pcc);
            } else {
                System.out.println("Already visited " + localUrl);
            }
        }
    }
}
