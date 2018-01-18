package com.stubbornjava.undertow.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.SetHeaderHandler;
import io.undertow.util.Headers;

// {{start:handler}}
public class StrictTransportSecurityHandlers {

    public static HttpHandler hsts(HttpHandler next, long maxAge) {
        return new SetHeaderHandler(next, Headers.STRICT_TRANSPORT_SECURITY_STRING, "max-age=" + maxAge);
    }

    public static HttpHandler hstsIncludeSubdomains(HttpHandler next, long maxAge) {
        return new SetHeaderHandler(next, Headers.STRICT_TRANSPORT_SECURITY_STRING, "max-age=" + maxAge + "; includeSubDomains");
    }
}
// {{end:handler}}
