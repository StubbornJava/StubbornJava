package com.stubbornjava.undertow.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.SetHeaderHandler;

// {{start:handler}}
public class XXssProtectionHandlers {
    private static final String X_XSS_PROTECTION_STRING = "X-Xss-Protection";

    public static HttpHandler disable(HttpHandler next) {
        return new SetHeaderHandler(next, X_XSS_PROTECTION_STRING, "0");
    }

    public static HttpHandler enable(HttpHandler next) {
        return new SetHeaderHandler(next, X_XSS_PROTECTION_STRING, "1");
    }

    public static HttpHandler enableAndBlock(HttpHandler next) {
        return new SetHeaderHandler(next, X_XSS_PROTECTION_STRING, "1; mode=block");
    }
}
// {{end:handler}}
