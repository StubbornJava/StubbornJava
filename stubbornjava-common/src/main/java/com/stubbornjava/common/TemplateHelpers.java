package com.stubbornjava.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.github.jknack.handlebars.Options;
import com.google.common.base.Strings;
import com.typesafe.config.Config;

public class TemplateHelpers {
    static final DateTimeFormatter MMMddyyyyFmt = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    public static CharSequence dateFormat(String dateString, Options options) {
        LocalDateTime date = LocalDateTime.parse(dateString);
        return MMMddyyyyFmt.format(date);
    }

    private static final String cdnHost = Configs.<String>getOrDefault(Configs.properties(),
                                                               "cdn.host",
                                                               Config::getString,
                                                               () -> null);
    // This expects the url to be relative (eg. /static/img.jpg)
    public static CharSequence cdn(String url) {
        if (Strings.isNullOrEmpty(cdnHost)) {
            return url;
        }
        return cdnHost + url;
    }
}
