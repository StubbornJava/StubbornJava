package com.stubbornjava.webapp;

import static com.stubbornjava.common.undertow.handlers.CustomHandlers.timed;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.seo.SitemapRoutes;
import com.stubbornjava.common.undertow.SimpleServer;
import com.stubbornjava.common.undertow.handlers.CustomHandlers;
import com.stubbornjava.undertow.handlers.ContentSecurityPolicyHandler;
import com.stubbornjava.undertow.handlers.ContentSecurityPolicyHandler.ContentSecurityPolicy;
import com.stubbornjava.undertow.handlers.MiddlewareBuilder;
import com.stubbornjava.undertow.handlers.ReferrerPolicyHandlers.ReferrerPolicy;
import com.stubbornjava.webapp.guide.GuideRoutes;
import com.stubbornjava.webapp.post.JavaLibRoutes;
import com.stubbornjava.webapp.post.PostRoutes;
import com.stubbornjava.webapp.themes.ThemeRoutes;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.PathHandler;

public class StubbornJavaWebApp {
    private static final Logger logger = LoggerFactory.getLogger(StubbornJavaWebApp.class);

    private static HttpHandler exceptionHandler(HttpHandler next) {
        return CustomHandlers.exception(next)
//         .addExceptionHandler(ResponseException.NotFound.class, HelperRoutes::notFound)
           .addExceptionHandler(Throwable.class, PageRoutes::error);
    }

    // {{start:csp}}
    private static HttpHandler contentSecurityPolicy(HttpHandler delegate) {
        return new ContentSecurityPolicyHandler.Builder()
                .defaultSrc(ContentSecurityPolicy.SELF)
                .scriptSrc(ContentSecurityPolicy.SELF.getValue(), "https://www.google-analytics.com")
                // Drop the wildcard when we host our own images.
                .imgSrc(ContentSecurityPolicy.SELF.getValue(), "https://www.google-analytics.com", "*")
                .connectSrc(ContentSecurityPolicy.SELF.getValue(), "https://www.google-analytics.com")
                .fontSrc(ContentSecurityPolicy.SELF.getValue(), "data:")
                .styleSrc(ContentSecurityPolicy.SELF.getValue(), ContentSecurityPolicy.UNSAFE_INLINE.getValue())
                .build(delegate);
    }
    // {{end:csp}}

    // {{start:middleware}}
    private static HttpHandler wrapWithMiddleware(HttpHandler next) {
        return MiddlewareBuilder.begin(PageRoutes::redirector)
                                .next(handler -> CustomHandlers.securityHeaders(handler, ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                                .next(StubbornJavaWebApp::contentSecurityPolicy)
                                .next(CustomHandlers::gzip)
                                .next(BlockingHandler::new)
                                .next(ex -> CustomHandlers.accessLog(ex, logger))
                                .next(CustomHandlers::statusCodeMetrics)
                                .next(StubbornJavaWebApp::exceptionHandler)
                                .complete(next);
    }
    // {{end:middleware}}

    // These routes do not require any authentication
    private static final HttpHandler getBasicRoutes() {
        return new RoutingHandler()
            .get("/", timed("getHome", PageRoutes::home))
            .get("/ping", timed("ping", PageRoutes::ping))

            .get("/favicon.ico", timed("favicon", CustomHandlers.resource("images/", (int)TimeUnit.DAYS.toSeconds(30))))
            .get("robots.txt", timed("robots", PageRoutes::robots))

            .get("/posts/{slug}", timed("getPost", PostRoutes::getPost))

            .get("/tags/{tag}/posts", timed("getRecentPostsWithTag", PostRoutes::recentPostsWithTag))

            .get("/java-libraries", timed("getJavaLibraries", JavaLibRoutes::listLibraries))
            .get("/java-libraries/{library}", timed("getLibrary", JavaLibRoutes::getLibrary))
            .get("/libraries/{library}/posts", timed("getLibraryRedirect", JavaLibRoutes::getLibraryRedirect))

            .get("/guides/{slug}", timed("getGuide", GuideRoutes::getGuide))

            .get("/best-selling-html-css-themes-and-website-templates", timed("popularThemes", ThemeRoutes::popularThemes))

            .get("/dev/metrics", timed("getMetrics", HelperRoutes::getMetrics))

            // addAll allows you to combine more than one RoutingHandler together.
            .addAll(SitemapRoutes.router(StubbornJavaSitemapGenerator.getSitemap()))

            .setFallbackHandler(timed("notFound", PageRoutes::notFound));
    }

    private static Undertow startServer() {
        HttpHandler staticRoutes = new PathHandler(getBasicRoutes())
            .addPrefixPath("/assets", timed("getAssets", CustomHandlers.resource("", (int)TimeUnit.DAYS.toSeconds(30))));
        SimpleServer server = SimpleServer.simpleServer(wrapWithMiddleware(staticRoutes));
        return server.start();
    }

    public static void main(String[] args) {
        StubbornJavaBootstrap.run(() -> {
            startServer();
        });
    }
}
