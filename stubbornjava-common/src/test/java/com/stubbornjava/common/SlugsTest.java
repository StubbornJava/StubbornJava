package com.stubbornjava.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SlugsTest {

    @Test
    public void testSimpleSlug() {
        String in = "2016-01-01 My Post";
        String result = Slugs.toSlug(in);
        String expected = "2016-01-01-my-post";
        assertEquals(expected, result);
    }

    @Test
    public void testSimpleSlugEndsWithoutSpecialChar() {
        String in = "2016-01-01 My Post?";
        String result = Slugs.toSlug(in);
        String expected = "2016-01-01-my-post";
        assertEquals(expected, result);
    }

    @Test
    public void testMultiInvalidCharSlug() {
        String in = "2016-01-01-- &&^%  My ++ Post";
        String result = Slugs.toSlug(in);
        String expected = "2016-01-01-my-post";
        assertEquals(expected, result);
    }

    @Test
    public void testNullSlug() {
        String in = null;
        String result = Slugs.toSlug(in);
        String expected = null;
        assertEquals(expected, result);
    }

    @Test
    public void testEmptySlug() {
        String in = "";
        String result = Slugs.toSlug(in);
        String expected = "";
        assertEquals(expected, result);
    }
}
