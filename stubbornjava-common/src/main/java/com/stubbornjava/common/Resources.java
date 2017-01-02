package com.stubbornjava.common;

import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;

public class Resources {

    public static String asString(String path) {
        URL url = com.google.common.io.Resources.getResource(path);
        try {
            return com.google.common.io.Resources.toString(url, Charsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
