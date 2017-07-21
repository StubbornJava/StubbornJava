package com.stubbornjava.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Slugs {
    // Match any non "word" ([a-zA-Z_0-9]) character
    private static final Pattern pattern = Pattern.compile("\\W+");

    public static String toSlug(String text) {
        if (null == text) {
            return null;
        }
        Matcher m = pattern.matcher(text);
        String result = m.replaceAll("-");
        if (result.endsWith("-")) {
            // Remove the last char if it's a dash
            result = result.substring(0, result.length() - 1);
        }
        return result.toLowerCase();
    }
}
