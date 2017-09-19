package com.richard.mccarthy;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

public class Page implements Comparable <Page>, Consumer <String> {

    private String domain;
    private String name;
    private Set<String> links;
    private Set<String> externalUrls;
    private Set<String> staticContent;


    public Page (String name, String domain) {
        this.name = name;
        this.domain = domain;
        this.links = new TreeSet<>();
        this.externalUrls = new TreeSet<>();
        this.staticContent = new TreeSet<>();
    }

    public Set<String> getLinks() {
        return links;
    }

    public Set<String> getExternalUrls() {
        return externalUrls;
    }

    public Set<String> getStaticContent() {
        return staticContent;
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    @Override
    public int compareTo(Page p) {
        return this.name.compareTo(p.getName());
    }

    @Override
    public void accept(String s) {

        // Internal links will start with a '/'
        if (s.startsWith("/")) {
            getLinks().add(s);
        } else if (s.toLowerCase().startsWith("http")) {

            // Check to see if this url of form http:// is for same domain, in which case strip of protocol and domain and form link
            if (s.toLowerCase().contains(domain)) {
                String link = s.substring(s.indexOf(domain) + domain.length());
                getLinks().add(link);
            } else {
                getExternalUrls().add(s);
            }
        }
    }
}
