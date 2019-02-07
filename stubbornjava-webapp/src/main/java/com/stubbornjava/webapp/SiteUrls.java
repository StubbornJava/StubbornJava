package com.stubbornjava.webapp;

public class SiteUrls {

    public static String postUrl(String slug) {
        return String.format("/posts/%s", slug);
    }
}
