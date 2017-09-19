package com.richard.mccarthy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PageRetriever {

    public Document getDocument (String pageName) throws IOException {
        return Jsoup.connect(pageName).get();
    }
}
