package com.stubbornjava.common.server;

import com.stubbornjava.undertow.ContentTypeSenders;

public class Senders implements ContentTypeSenders {
    private static final Senders DEFAULT = new Senders();

    /*
     * Using a static global for simplicity. Use your own DI method however you want.
     */
    public static Senders send() {
        return DEFAULT;
    }
}
