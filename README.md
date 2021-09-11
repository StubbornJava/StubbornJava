[![Release](https://jitpack.io/v/StubbornJava/StubbornJava.svg)](https://jitpack.io/#StubbornJava/StubbornJava)
[![Follow @StubbornJava](https://img.shields.io/twitter/follow/stubbornJava.svg?style=social)](https://twitter.com/intent/follow?screen_name=StubbornJava) 



# StubbornJava
https://www.stubbornjava.com/

This is very much a work in progress and in the early stages. No code will be pushed to maven central or supported in any way currently. Feel free to clone and install locally. The website is built using all the methods described on the site and in the examples. There is no backing blog framework.

Potentially moving to [GitLab](https://gitlab.com/stubbornjava/StubbornJava)

## Quick Example (full example [Simple REST server in Undertow](https://www.stubbornjava.com/posts/lightweight-embedded-java-rest-server-without-a-framework))

```java
public static void createUser(HttpServerExchange exchange) {
    User userInput = userRequests.user(exchange);
    User user = userDao.create(userInput.getEmail(), userInput.getRoles());
    if (null == user) {
        ApiHandlers.badRequest(exchange, String.format("User %s already exists.", userInput.getEmail()));
        return;
    }
    exchange.setStatusCode(StatusCodes.CREATED);
    Exchange.body().sendJson(exchange, user);
}

public static void getUser(HttpServerExchange exchange) {
    String email = userRequests.email(exchange);
    User user = userDao.get(email);
    if (null == user) {
        ApiHandlers.notFound(exchange, String.format("User %s not found.", email));
        return;
    }
    Exchange.body().sendJson(exchange, user);
}
    
public static final RoutingHandler ROUTES = new RoutingHandler()
    .get("/users/{email}", timed("getUser", UserRoutes::getUser))
    .post("/users", timed("createUser", UserRoutes::createUser))
    .get("/metrics", timed("metrics", CustomHandlers::metrics))
    .get("/health", timed("health", CustomHandlers::health))
    .setFallbackHandler(timed("notFound", RoutingHandlers::notFoundHandler));

public static final HttpHandler ROOT = CustomHandlers.exception(EXCEPTION_THROWER)
    .addExceptionHandler(ApiException.class, ApiHandlers::handleApiException)
    .addExceptionHandler(Throwable.class, ApiHandlers::serverError);

public static void main(String[] args) {
    SimpleServer server = SimpleServer.simpleServer(Middleware.common(ROOT));
    server.start();
}
```

# Suggest a Topic
Check out [issues](https://github.com/StubbornJava/StubbornJava/issues) to suggest topics, bug fixes, errors, or vote on issues. Reactions / comments may influence the order topics are added but no guarantees. Several topics will not be accepted here such as larger frameworks (Spring, Play, Jersey ...) as well as dependency injection. We will be more focused on rolling things yourself and solving practical problems. The coding style here may not fit with the norm but it should be very easy to convert any of the classes to be DI friendly.

# Getting Started
A [guide for building your own minimal embedded Java web server](https://www.stubbornjava.com/guides/embedded-java-web-server). A simple in order intro to a lot of uses cases for simple web development.

# Dev Tools
* [Creating a local development environment with Docker Compose](https://www.stubbornjava.com/posts/creating-a-local-development-environment-with-docker-compose) (MySQL, Elasticsearch)
* [Configuring servers with Ansible](https://www.stubbornjava.com/posts/installing-java-supervisord-and-other-service-dependencies-with-ansible)

# HTML / CSS Themes and Templates for rapid prototyping
* [HTML / CSS Themes and Templates](https://www.stubbornjava.com/best-selling-html-css-themes-and-website-templates)

# Libraries
## [SLF4J and Logback for Logging](https://www.stubbornjava.com/posts/logging-in-java-with-slf4j-and-logback)
SLF4J is fairly standard and we chose to use Logback as our underlying implementation.

## [Typesafe Config For Configuration](https://www.stubbornjava.com/posts/environment-aware-configuration-with-typesafe-config)
Typesafe config is a clean lightweight immutable configuration library. It offers several formats such as `.properties`, `.yml`, `.json` as well as a human friendly json super set. It handles configuration inheritance, includes, data types (string, boolean, int, long, double, durations, arrays, ...), variabe substitution and many more features. [Typesafe Config examples](https://www.stubbornjava.com/posts/typesafe-config-features-and-example-usage)

## Jackson for JSON
* [Practical Jackson ObjectMapper Configuration](https://www.stubbornjava.com/posts/practical-jackson-objectmapper-configuration)
* [Somewhat Deterministic ObjectMapper](https://www.stubbornjava.com/posts/creating-a-somewhat-deterministic-jackson-objectmapper)

## [Embedded Undertow Web Server](https://www.stubbornjava.com/posts/java-hello-world-embedded-http-server-using-undertow)
Undertow is a very fast low level non blocking web server written in Java. It is very lightweight and has a very clean API that should be relatively easy for anyone who knows HTTP to pick up. Most custom code will be in the form of an HttpHandler which is a simple interface that can be used in a variety of ways.
* [Writing Custom HttpHandlers](https://www.stubbornjava.com/posts/undertow-writing-custom-httphandlers)
* [Url Routing in Undertow](https://www.stubbornjava.com/posts/url-routing-with-undertow-embedded-http-server)
* [Handling Exceptions in Undertow](https://www.stubbornjava.com/posts/handling-exceptions-in-undertow-with-composition)
* [Creating Middleware in Undertow](https://www.stubbornjava.com/posts/logging-gzip-blocking-exception-handling-metrics-middleware-chaining-in-undertow)
* [Simple REST server in Undertow](https://www.stubbornjava.com/posts/lightweight-embedded-java-rest-server-without-a-framework)
* [Virtual Hosting in Undertow](https://www.stubbornjava.com/posts/virtual-hosting-in-undertow-s-embedded-java-web-server)
* [Webpack and npm with Java](https://www.stubbornjava.com/posts/webpack-and-npm-for-simple-java-8-web-apps)
* [Sharing routes and running multiple webservers in a single JVM](https://www.stubbornjava.com/posts/sharing-routes-and-running-multiple-java-services-in-a-single-jvm-with-undertow)
* [Configuring Security Headers in Undertow](https://www.stubbornjava.com/posts/configuring-security-headers-in-undertow)
* [Circuit Breaking HttpHandler](https://www.stubbornjava.com/posts/increasing-resiliency-with-circuit-breakers-in-your-undertow-web-server-with-failsafe)
* [Creating a non-blocking delay in the Undertow Web Server for Artificial Latency](https://www.stubbornjava.com/posts/creating-a-non-blocking-delay-in-the-undertow-web-server-for-artificial-latency)

## Metrics (Dropwizard Metrics, Grafana, Graphite)
* [Monitoring your JVM with Dropwizard Metrics](https://www.stubbornjava.com/posts/monitoring-your-jvm-with-dropwizard-metrics)
* [Grafana Cloud Dropwizard Metrics Reporter](https://www.stubbornjava.com/posts/grafana-cloud-dropwizard-metrics-reporter)

## OkHttp for HTTP Client
* [OkHttp Example REST client](https://www.stubbornjava.com/posts/okhttp-example-rest-client)
* [OkHttp Logging Interceptors](https://www.stubbornjava.com/posts/okhttpclient-logging-configuration-with-interceptors)
* [OkHttpClient Trust All SSL Certificates](https://www.stubbornjava.com/posts/okhttpclient-trust-all-ssl-certificates)
* [Web Scraping with OkHttp and jsoup](https://www.stubbornjava.com/posts/web-scraping-in-java-using-jsoup-and-okhttp)

## [HikariCP for JDBC Connection Pooling](https://www.stubbornjava.com/posts/database-connection-pooling-in-java-with-hikaricp)
HikariCP is a very fast lightweight Java connection pool. The API and overall codebase is relatively small (A good thing) and highly optimized. It also does not cut corners for performance like many other Java connection pool implementations.

## Uility Classes / Extras
* [Password Hashing with BCrypt](https://www.stubbornjava.com/posts/hashing-passwords-in-java-with-bcrypt)
* [XML sitemaps for SEO](https://www.stubbornjava.com/posts/creating-xml-sitemaps-in-java)
* [Lazy loading and caching objects in Java with Guava's Suppliers.memoize](https://www.stubbornjava.com/posts/lazy-loading-and-caching-objects-in-java-with-guava-s-suppliers-memoize)
* [Reading File Resources with Guava](https://www.stubbornjava.com/posts/reading-file-resources-with-guava)

# Sites Built With This Method
* [StubbornJava](https://www.stubbornjava.com/)
* [QuickDraw](https://quickdraw.onsightdigitalsolutions.com/about)
* [DeckHandHQ](https://www.deckhandhq.com)
