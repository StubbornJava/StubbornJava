package com.stubbornjava.webapp.post;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SlugUtilTest {

    @Test
    public void testSimpleSlug() {
        String in = "2016-01-01 My Post";
        String result = SlugUtil.toSlug(in);
        String expected = "2016-01-01-my-post";
        assertEquals(expected, result);
    }

    @Test
    public void testSimpleSlugEndsWithoutSpecialChar() {
        String in = "2016-01-01 My Post?";
        String result = SlugUtil.toSlug(in);
        String expected = "2016-01-01-my-post";
        assertEquals(expected, result);
    }

    @Test
    public void testMultiInvalidCharSlug() {
        String in = "2016-01-01-- &&^%  My ++ Post";
        String result = SlugUtil.toSlug(in);
        String expected = "2016-01-01-my-post";
        assertEquals(expected, result);
    }

    @Test
    public void testNullSlug() {
        String in = null;
        String result = SlugUtil.toSlug(in);
        String expected = null;
        assertEquals(expected, result);
    }

    @Test
    public void testEmptySlug() {
        String in = "";
        String result = SlugUtil.toSlug(in);
        String expected = "";
        assertEquals(expected, result);
    }
}
