package com.richard.mccarthy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PageTest {

    @Test
    public void constructorTest () {
        Page p = new Page ("name", "domain");

        assertNotNull (p);
        assertEquals("name", p.getName());
        assertEquals("domain", p.getDomain());

        assertNotNull(p.getLinks());
        assertNotNull(p.getExternalUrls());
        assertNotNull(p.getStaticContent());

        assertTrue(p.getLinks().size() == 0);
        assertTrue(p.getExternalUrls().size() == 0);
        assertTrue(p.getStaticContent().size() == 0);
    }

    @Test
    public void compareToTest () {
        Page p = new Page ("name", "domain");
        Page p2 = new Page ("name2", "domain");

        assertNotNull (p);
        assertNotNull (p2);
        assertTrue (p.compareTo(p2) == -1);
        assertTrue (p2.compareTo(p) == 1);
    }

    @Test
    public void acceptWhenLink () {
        Page p = new Page ("name", "domain");

        p.accept("/myLink");

        assertTrue(p.getLinks().size() == 1);
        assertTrue(p.getExternalUrls().size() == 0);
        assertTrue(p.getStaticContent().size() == 0);
    }

    @Test
    public void acceptWhenNotUnderstood () {
        Page p = new Page ("name", "domain");

        p.accept("abcd");

        assertTrue(p.getLinks().size() == 0);
        assertTrue(p.getExternalUrls().size() == 0);
        assertTrue(p.getStaticContent().size() == 0);
    }

    @Test
    public void acceptWhenDomainLink () {
        Page p = new Page ("name", "domain");

        p.accept("http://domain");

        assertTrue(p.getLinks().size() == 1);
        assertTrue(p.getExternalUrls().size() == 0);
        assertTrue(p.getStaticContent().size() == 0);
    }

    @Test
    public void acceptWhenExternalLink () {
        Page p = new Page ("name", "domain");

        p.accept("http://external");

        assertTrue(p.getLinks().size() == 0);
        assertTrue(p.getExternalUrls().size() == 1);
        assertTrue(p.getStaticContent().size() == 0);
    }
}
