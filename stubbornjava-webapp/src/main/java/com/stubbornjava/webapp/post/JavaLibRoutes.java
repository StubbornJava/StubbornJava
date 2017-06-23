package com.stubbornjava.webapp.post;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.webapp.Response;

import io.undertow.server.HttpServerExchange;

public class JavaLibRoutes {
    private static final Logger logger = LoggerFactory.getLogger(JavaLibRoutes.class);

    public static void listLibraries(HttpServerExchange exchange) {
        List<JavaLib> libs = JavaLib.sorted();

        Response response = Response.fromExchange(exchange)
                .with("javaLibs", libs)
                .withLibCounts()
                .withRecentPosts();
        Exchange.body().sendHtmlTemplate(exchange, "templates/src/widgets/recommendations/java-libraries", response);
    }

    public static void getLibrary(HttpServerExchange exchange) {
        String lib = Exchange.pathParams().pathParam(exchange, "library").orElse("");
        List<PostMeta> posts = Posts.getRecentPostsWithLibrary(lib);
        JavaLib library = JavaLib.findByName(lib);
        Response response = Response.fromExchange(exchange)
                .with("posts", posts)
                .with("type", "Library")
                .with("value", lib)
                .with("library", library)
                .withLibCounts()
                .withRecentPosts();
        Exchange.body().sendHtmlTemplate(exchange, "templates/src/pages/library", response);
    }

    // This should be removed eventually. Needed to redirect old urls.
    public static void getLibraryRedirect(HttpServerExchange exchange) {
        String url = Exchange.urls().currentUrl(exchange).toString();
        String newUrl = url.replace("/libraries/", "/java-libraries/")
                           .replace("/posts", "/");
        logger.debug("Redirecting {} to {}", url, newUrl);
        Exchange.redirect().permanent(exchange, newUrl);
    }
}
