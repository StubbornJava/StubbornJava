package com.stubbornjava.common.undertow;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Undertow;
import io.undertow.Undertow.ListenerInfo;
import io.undertow.server.HttpHandler;

public class UndertowUtil {
    private static final Logger logger = LoggerFactory.getLogger(UndertowUtil.class);

    /**
     * This is currently intended to be used in unit tests but may
     * be appropriate in other situations as well. It's not worth building
     * out a test module at this time so it lives here.
     *
     * This helper will spin up the http handler on a random available port.
     * The full host and port will be passed to the hostConsumer and the server
     * will be shut down after the consumer completes.
     *
     * @param builder
     * @param handler
     * @param hostConusmer
     */
    public static void useLocalServer(Undertow.Builder builder,
                                      HttpHandler handler,
                                      Consumer<String> hostConusmer) {
        Undertow undertow = null;
        try  {
            // Starts server on a random open port
            undertow = builder.addHttpListener(0, "127.0.0.1", handler).build();
            undertow.start();
            ListenerInfo listenerInfo = undertow.getListenerInfo().get(0);
            InetSocketAddress addr = (InetSocketAddress) listenerInfo.getAddress();
            String host = "http://localhost:" + addr.getPort();
            hostConusmer.accept(host);
        } finally {
            if (undertow != null) {
                undertow.stop();
            }
        }
    }
}
