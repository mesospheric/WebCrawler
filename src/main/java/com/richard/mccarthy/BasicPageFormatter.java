package com.richard.mccarthy;


public class BasicPageFormatter {

    public static String format(Page page) {

        // A crude formatter for a page and the links, static content & external urls it contains
        StringBuilder sb = new StringBuilder();
        String linefeed = System.getProperty("line.separator");

        sb.append("Page Name : ").append(page.getName()).append(linefeed).append(linefeed);
        sb.append("Internal Links").append(linefeed);

        for (String s : page.getLinks()) {
            sb.append("    ").append(s).append(linefeed);
        }
        sb.append(linefeed).append("External Links").append(linefeed);
        for (String s : page.getExternalUrls()) {
            sb.append("    ").append(s).append(linefeed);
        }
        sb.append(linefeed).append("Static Content").append(linefeed);
        for (String s : page.getStaticContent()) {
            sb.append("    ").append(s).append(linefeed);
        }
        sb.append(linefeed);

        return sb.toString();
    }
}
