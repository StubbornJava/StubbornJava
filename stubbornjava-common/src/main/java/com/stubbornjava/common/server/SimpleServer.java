package com.stubbornjava.common.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;

public class SimpleServer {
    private static final Logger logger = LoggerFactory.getLogger(SimpleServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "localhost";

    private final Undertow undertow;
    private SimpleServer(Undertow undertow) {
        this.undertow = undertow;
    }

    /*
     * As you can see this class is not meant to abstract away the Undertow server,
     * its goal is simply to have some common configurations. We expose Undertow
     * if a different service needs to modify it in any way before we call start.
     */
    public Undertow getUndertow() {
        return undertow;
    }

    public void start() {
        undertow.start();
        /*
         *  Undertow logs this on its own but we generally set 3rd party
         *  default logger levels to warn so we log it here. If it wasn't using the
         *  io.undertow context we could turn on just that logger but no big deal.
         */
        undertow.getListenerInfo()
                .stream()
                .forEach(listenerInfo -> logger.debug(listenerInfo.toString()));
    }

    public static SimpleServer simpleServer(HttpHandler handler) {
        Undertow undertow = Undertow.builder()
            /*
             * This setting is needed if you want to allow '=' as a value in a cookie.
             * If you base64 encode any cookie values you probably want it on.
             */
            .setServerOption(UndertowOptions.ALLOW_EQUALS_IN_COOKIE_VALUE, true)
            .addHttpListener(DEFAULT_PORT, DEFAULT_HOST, handler)
            .build()
        ;
        return new SimpleServer(undertow);
    }
}
