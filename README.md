# StubbornJava
https://www.stubbornjava.com/

This is very much a work in progress and in the early stages. No code will be pushed to maven or supported in any way currently. Feel free to clone and install locally. Currently the website itself is not open souced but that is the eventual plan. The website is built using all the methods described on the site and in the examples. There is no backing blog framework.

#Libraries
##[SLF4J and Logback for Logging](https://www.stubbornjava.com/posts/logging-in-java-with-slf4j-and-logback)
SLF4J is fairly standard and we chose to use Logback as our underlying implementation.

##[Typesafe Config For Configuration](https://www.stubbornjava.com/posts/environment-aware-configuration-with-typesafe-config)
Typesafe config is a clean lightweight immutable configuration library. It offers several formats such as `.properties`, `.yml`, `.json` as well as a human friendly json super set. It handles configuration inheritance, includes, data types (string, boolean, int, long, double, durations, arrays, ...), variabe substitution and many more features.

##[Embedded Undertow Web Server](https://www.stubbornjava.com/posts/java-hello-world-embedded-http-server-using-undertow)
Undertow is a very fast low level non blocking web server written in Java. It is very lightweight and has a very clean API that should be relatively easy for anyone who knows HTTP to pick up. Most custom code will be in the form of an HttpHandler which is a simple interface that can be used in a variety of ways.
* [Writing Custom HttpHandlers](https://www.stubbornjava.com/posts/undertow-writing-custom-httphandlers)
* [Url Routing in Undertow](https://www.stubbornjava.com/posts/url-routing-with-undertow-embedded-http-server)
* [Handling Exceptions in Undertow](https://www.stubbornjava.com/posts/handling-exceptions-in-undertow-with-composition)
* [Creating Middleware in Undertow](https://www.stubbornjava.com/posts/logging-gzip-blocking-exception-handling-metrics-middleware-chaining-in-undertow)
* [Simple REST server in Undertow](https://www.stubbornjava.com/posts/lightweight-embedded-java-rest-server-without-a-framework)

##[HikariCP for JDBC Connection Pooling](https://www.stubbornjava.com/posts/database-connection-pooling-in-java-with-hikaricp)
HikariCP is a very fast lightweight Java connection pool. The API and overall codebase is relatively small (A good thing) and highly optimized. It also does not cut corners for performance like many other Java connection pool implementations.

## Sites Built With This Method
* [StubbornJava](https://www.stubbornjava.com/)
* [QuickDraw Affiliate Reporting](https://quickdraw.onsightdigitalsolutions.com/about)
