package com.stubbornjava.examples.undertow.handlebars;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stubbornjava.common.undertow.Exchange;

import io.undertow.server.HttpServerExchange;

public class HandlebarsHandlers {

    // {{start:message}}
    private static final String messageTemplate = "<p>hello {{message}}</p>";
    public static void messageRawTemplate(HttpServerExchange exchange) {
        String message = Exchange.queryParams().queryParam(exchange, "message").orElse("world");
        Map<String, Object> data = Maps.newHashMap();
        data.put("message", message);
        Exchange.body().sendRawHtmlTemplate(exchange, messageTemplate, data);
    }
    // {{end:message}}

    // {{start:messages}}
    private static final String messagesTemplate = "{{#each messages}}<p>Hello {{.}}</p>{{/each}}";
    public static void messagesRawTemplate(HttpServerExchange exchange) {
        String message = Exchange.queryParams().queryParam(exchange, "message").orElse("world");
        int num = Exchange.queryParams().queryParamAsInteger(exchange, "num").orElse(5);
        List<String> messages = Lists.newArrayList();
        for (int i = 0; i < num; i++) {
            messages.add(message + " " + i);
        }
        Map<String, Object> data = Maps.newHashMap();
        data.put("messages", messages);
        Exchange.body().sendRawHtmlTemplate(exchange, messagesTemplate, data);
    }
    // {{end:messages}}

    // {{start:messagesFull}}
    public static void messagesTemplate(HttpServerExchange exchange) {
        String message = Exchange.queryParams().queryParam(exchange, "message").orElse("world");
        int num = Exchange.queryParams().queryParamAsInteger(exchange, "num").orElse(5);
        List<String> messages = Lists.newArrayList();
        for (int i = 0; i < num; i++) {
            messages.add(message + " " + i);
        }
        Map<String, Object> data = Maps.newHashMap();
        data.put("messages", messages);
        Exchange.body().sendHtmlTemplate(exchange, "examples/handlebars/messages", data);
    }
    // {{end:messagesFull}}

}
