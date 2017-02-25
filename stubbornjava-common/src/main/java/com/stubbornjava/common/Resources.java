package com.stubbornjava.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.CharSource;

public class Resources {

    // {{start:asString}}
    public static String asString(String resource) {
        URL url = com.google.common.io.Resources.getResource(resource);
        try {
            return com.google.common.io.Resources.toString(url, Charsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    // {{end:asString}}

    // {{start:asBufferedReader}}
    public static BufferedReader asBufferedReader(String resource) {
        URL url = com.google.common.io.Resources.getResource(resource);
        try {
            CharSource charSource = com.google.common.io.Resources.asCharSource(url, Charsets.UTF_8);
            return charSource.openBufferedStream();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    // {{end:asBufferedReader}}
}
