package com.stubbornjava.common.undertow;

import com.stubbornjava.common.Templating;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public interface HtmlTemplateSender {

    // {{start:sender}}
    default void sendRawHtmlTemplate(HttpServerExchange exchange, String rawTemplate, Object data) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        exchange.getResponseSender().send(Templating.instance().renderRawHtmlTemplate(rawTemplate, data));
    }

    default void sendHtmlTemplate(HttpServerExchange exchange, String templateName, Object data) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
        exchange.getResponseSender().send(Templating.instance().renderHtmlTemplate(templateName, data));
    }
    // {{end:sender}}
}
