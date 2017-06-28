package com.stubbornjava.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import okhttp3.HttpUrl;

public class HttpUrlsTest {

    @Test
    public void testDomain() {
        HttpUrl url = HttpUrl.parse("http://www.stubbornjava.com");
        HttpUrl actual = HttpUrls.host(url);
        HttpUrl expected = HttpUrl.parse("http://www.stubbornjava.com");

        assertEquals(expected, actual);
    }

    @Test
    public void testDomainTrailingSlash() {
        HttpUrl url = HttpUrl.parse("http://www.stubbornjava.com/");
        HttpUrl actual = HttpUrls.host(url);
        HttpUrl expected = HttpUrl.parse("http://www.stubbornjava.com");

        assertEquals(expected, actual);
    }

    @Test
    public void testDomainWithPath() {
        HttpUrl url = HttpUrl.parse("http://www.stubbornjava.com/path1");
        HttpUrl actual = HttpUrls.host(url);
        HttpUrl expected = HttpUrl.parse("http://www.stubbornjava.com");

        assertEquals(expected, actual);
    }

    @Test
    public void testDomainWithPaths() {
        HttpUrl url = HttpUrl.parse("http://www.stubbornjava.com/path1/path2");
        HttpUrl actual = HttpUrls.host(url);
        HttpUrl expected = HttpUrl.parse("http://www.stubbornjava.com");

        assertEquals(expected, actual);
    }

    @Test
    public void testDomainWithEmptyQueryString() {
        HttpUrl url = HttpUrl.parse("http://www.stubbornjava.com?");
        HttpUrl actual = HttpUrls.host(url);
        HttpUrl expected = HttpUrl.parse("http://www.stubbornjava.com");

        assertEquals(expected, actual);
    }

    @Test
    public void testDomainWithQueryString() {
        HttpUrl url = HttpUrl.parse("http://www.stubbornjava.com?x=1");
        HttpUrl actual = HttpUrls.host(url);
        HttpUrl expected = HttpUrl.parse("http://www.stubbornjava.com");

        assertEquals(expected, actual);
    }

    @Test
    public void testDomainWithPathsAndQueryString() {
        HttpUrl url = HttpUrl.parse("http://www.stubbornjava.com/path1/path2?x=1");
        HttpUrl actual = HttpUrls.host(url);
        HttpUrl expected = HttpUrl.parse("http://www.stubbornjava.com");

        assertEquals(expected, actual);
    }
}
