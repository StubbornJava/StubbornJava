package com.stubbornjava.common;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;

import org.junit.Test;

import com.google.common.io.CharStreams;

public class ResourcesTest {

    // {{start:asString}}
    @Test
    public void testAsString() {
        String expected = "hello\nworld\n";
        String actual = Resources.asString("resources-test/hello.txt");
        assertEquals(expected, actual);
    }
    // {{end:asString}}

    // {{start:asBufferedReader}}
    @Test
    public void testAsBufferedReader() throws IOException {
        String expected = "hello\nworld\n";
        BufferedReader reader = Resources.asBufferedReader("resources-test/hello.txt");
        String actual = CharStreams.toString(reader);
        assertEquals(expected, actual);
    }
    // {{end:asBufferedReader}}
}
