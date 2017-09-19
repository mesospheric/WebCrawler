package com.richard.mccarthy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class PageCrawlerCallableTest {

    ExecutorService mockExecutorService = mock(ExecutorService.class);
    PageRetriever mockPageRetriever = mock (PageRetriever.class);
    Page page;
    ConcurrentHashMap<String, Page> siteMap;

    @Before
    public void setup () {
        page = new Page ("name", "domain");
        siteMap = new ConcurrentHashMap<>();
    }

    @Test
    public void testFactoryMethod() {

        PageCrawlerCallable pcc = PageCrawlerCallable.createCallable(page, siteMap, mockExecutorService);
        assertNotNull (pcc);
    }

    @Test
    public void testCallOnNoContent() {
        Document doc = new Document("baseuri");

        PageCrawlerCallable pcc = PageCrawlerCallable.createCallable(page, siteMap, mockExecutorService, mockPageRetriever);

        try {
            when(mockPageRetriever.getDocument(anyString())).thenReturn(doc);
            //doNothing().when (mockExecutorService).submit(any(PageCrawlerCallable.class));
            page = pcc.call();

            assertTrue(page.getLinks().size() == 0);
            assertTrue(page.getExternalUrls().size() == 0);
            assertTrue(page.getStaticContent().size() == 0);

            verify(mockPageRetriever).getDocument(anyString());
            verifyZeroInteractions(mockExecutorService);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCallTwoMedia() {
        Document doc = loadHtmlFile("/single-media-import.html");
        PageCrawlerCallable pcc = PageCrawlerCallable.createCallable(page, siteMap, mockExecutorService, mockPageRetriever);

        try {
            when(mockPageRetriever.getDocument(anyString())).thenReturn(doc);

            page = pcc.call();

            assertNotNull(page);
            assertEquals ("name", page.getName());

            assertTrue(page.getLinks().size() == 0);
            assertTrue(page.getExternalUrls().size() == 0);
            assertTrue(page.getStaticContent().size() == 2);

            assertTrue(page.getStaticContent().contains("/assets/react-0.13.3/react-0.13.3-7a4fa7bc05e298c09c8db863f132ba22.js"));
            assertTrue(page.getStaticContent().contains("/assets/favicon/apple-touch-152-63d7a4c42a1ab2ef3d086d87b05927ef.png"));

            verify(mockPageRetriever).getDocument(anyString());
            verifyZeroInteractions(mockExecutorService);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }


    @Test
    public void testCallLinks() {
        Document doc = loadHtmlFile("/ThreeLinkTypes.html");
        PageCrawlerCallable pcc = PageCrawlerCallable.createCallable(page, siteMap, mockExecutorService, mockPageRetriever);

        try {
            when(mockPageRetriever.getDocument(anyString())).thenReturn(doc);

            page = pcc.call();

            assertNotNull(page);
            assertEquals ("name", page.getName());

            assertTrue(page.getLinks().size() == 2);
            assertTrue(page.getExternalUrls().size() == 1);
            assertTrue(page.getStaticContent().size() == 0);

            assertTrue(page.getExternalUrls().contains("https://facebook.com"));
            assertTrue(page.getLinks().contains("/ManningBooks"));
            assertTrue(page.getLinks().contains("/account"));

            verify(mockPageRetriever).getDocument(anyString());
            verify(mockExecutorService, times(2)).submit(Matchers.<PageCrawlerCallable>any());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
    private Document loadHtmlFile (String filename) {

        Document doc = null;
        InputStream in = this.getClass().getResourceAsStream(filename);
        try {
            doc =  Jsoup.parse(in, "UTF-8", "manning.com");

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("");
        }
        return doc;
    }
}
