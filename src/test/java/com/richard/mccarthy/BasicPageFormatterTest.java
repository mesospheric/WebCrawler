package com.richard.mccarthy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BasicPageFormatterTest {

    @Test
    public void testTitlesNoContent () {
        Page page = new Page("name", "domain");
        String linefeed = System.getProperty("line.separator");

        String result = BasicPageFormatter.format(page);

        assertNotNull(result);
        assertEquals ("Page Name : name" +
                linefeed +
                linefeed +
                "Internal Links" +
                linefeed +
                linefeed +
                "External Links" +
                linefeed +
                linefeed +
                "Static Content" +
                linefeed +
                linefeed
                            , result);

    }

    @Test
    public void testTitlesOneOfEach () {
        Page page = new Page("name", "domain");
        page.getLinks().add("link");
        page.getStaticContent().add("static");
        page.getExternalUrls().add("external");
        String linefeed = System.getProperty("line.separator");

        String result = BasicPageFormatter.format(page);

        assertNotNull(result);
        assertEquals ("Page Name : name" +
                        linefeed +
                        linefeed +
                        "Internal Links" + linefeed +
                        "    link" +
                        linefeed +
                        linefeed +
                        "External Links" + linefeed +
                        "    external" +
                        linefeed +
                        linefeed +
                        "Static Content" + linefeed +
                        "    static" +
                        linefeed +
                        linefeed
                , result);

    }
}
