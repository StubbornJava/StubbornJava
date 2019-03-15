package com.stubbornjava.webapp.post;

import java.time.LocalDateTime;
import java.util.List;

import com.google.common.collect.Lists;
import com.stubbornjava.webapp.github.FileReference;

public class PostData {
    private static final List<PostRaw> posts = Lists.newLinkedList();
    static {
        posts.add(PostRaw.builder()
            .postId(811794634041215289L)
            .title("Practical Jackson ObjectMapper Configuration")
            .metaDesc("Create, configure, and add modules including java.time to a Jackson ObjectMapper for JSON serialization and deserialization in Java.")
            .dateCreated(LocalDateTime.parse("2016-12-21T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2016-12-21T10:15:30"))
            .javaLib(JavaLib.Jackson)
            .tags(Lists.newArrayList(Tags.JSON))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                                 "jackson",
                                 "stubbornjava-common/src/main/java/com/stubbornjava/common/Json.java")
                , FileReference.stubbornJava(
                                   "test",
                                   "stubbornjava-common/src/test/java/com/stubbornjava/common/JsonTest.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(815810734072936393L)
            .title("Environment aware configuration with Typesafe Config")
            .metaDesc("Using Typesafe Config for default configuration with environment specific overrides. Share common configs and override in production / uat")
            .dateCreated(LocalDateTime.parse("2017-01-02T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-02T10:15:30"))
            .javaLib(JavaLib.TypesafeConfig)
            .tags(Lists.newArrayList(Tags.Configuration))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                                 "config",
                                 "stubbornjava-common/src/main/java/com/stubbornjava/common/Configs.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(815948056762178816L)
            .title("Thoughts on Configuration")
            .metaDesc("Reduce and reuse application configurations as much as possible, favor hard coding non configurable values.")
            .dateCreated(LocalDateTime.parse("2017-01-02T12:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-02T12:15:30"))
            .tags(Lists.newArrayList(Tags.Configuration))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(818151438688885573L)
            .title("Java Hello World Embedded HTTP server using Undertow")
            .metaDesc("Java HTTP server example with Undertow. Creating a simple hello world web server with Undertow. Runs in any IDE or as a standalone JAR, no application containers required.")
            .dateCreated(LocalDateTime.parse("2017-01-08T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-08T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Undertow))
            .tags(Lists.newArrayList(Tags.WebServer))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                                 "helloworld",
                                 "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/helloworld/HelloWorldServer.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(818171591721755641L)
            .title("Undertow - Writing Custom HttpHandlers")
            .metaDesc("Multiple approaches to creating custom Undertow HttpHandlers the building blocks of handling requests.")
            .dateCreated(LocalDateTime.parse("2017-01-08T12:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-08T12:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Undertow))
            .tags(Lists.newArrayList(Tags.WebServer))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                                 "handler",
                                 "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/routing/ConstantStringHandler.java")
                , FileReference.stubbornJava(
                                    "handlers",
                                    "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/routing/RoutingHandlers.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(818247342407340249L)
            .title("URL Routing with Undertow embedded HTTP Server")
            .metaDesc("Embedded Undertow java web server with url routing. Annotationless routing for simplicity and zero magic.")
            .dateCreated(LocalDateTime.parse("2017-01-08T14:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-08T14:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Undertow))
            .tags(Lists.newArrayList(Tags.WebServer))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                                 "routing",
                                 "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/routing/RoutingServer.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(818597723874283145L)
            .title("What is a Content Type?")
            .metaDesc("What is an HTTP Content-Type (text/plain, text/html, application/json, application/octet-stream) and what is it used for?")
            .dateCreated(LocalDateTime.parse("2017-01-09T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-09T10:15:30"))
            .javaLibs(Lists.newArrayList())
            .tags(Lists.newArrayList(Tags.HTTP))
            .gitFileReferences(Lists.newArrayList())
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(818598236155825977L)
            .title("Handling Content Types with Undertow")
            .metaDesc("Creating Undertow HttpHandlers for handing Content-Types (text/plain, text/html, application/json, application/octet-stream)")
            .dateCreated(LocalDateTime.parse("2017-01-09T14:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-09T14:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Undertow))
            .tags(Lists.newArrayList(Tags.WebServer, Tags.HTTP))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                                 "handlers",
                                 "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/contenttypes/ContentTypeHandlers.java")
                , FileReference.stubbornJava(
                                    "server",
                                    "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/contenttypes/ContentTypesServer.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(818954485044385193L)
            .title("Handling Exceptions in Undertow with Composition")
            .metaDesc("Creating a composable Undertow ExceptionHandler middleware for unexpected scenarios. Using plain HttpHandlers to handle exceptions there is no special interface for ExceptionHandling in Undertow.")
            .dateCreated(LocalDateTime.parse("2017-01-10T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-10T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Undertow))
            .tags(Lists.newArrayList(Tags.WebServer, Tags.ExceptionHandling, Tags.Logging, Tags.Middleware))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                             "handlers",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/exceptionhandling/ExceptionHandlers.java")
                , FileReference.stubbornJava(
                               "server",
                               "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/exceptionhandling/ExceptionHandlingServer.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(819163445045032078L)
            .title("HTTP Access Logging with Undertow")
            .metaDesc("Embedded Undertow java web server access logging handler. Use SLF4J to handle request logging with Undertow.")
            .dateCreated(LocalDateTime.parse("2017-01-11T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-11T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Undertow, JavaLib.SLF4J))
            .tags(Lists.newArrayList(Tags.WebServer, Tags.Logging, Tags.HTTP, Tags.Middleware))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                             "server",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/accesslog/AccessLogServer.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(819687313936498152L)
            .title("Query Parameters and Path Parameters in Undertow")
            .metaDesc("Accessing Undertow query string parameters and path parameters with helpers. Use Optional for defaults.")
            .dateCreated(LocalDateTime.parse("2017-01-12T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-12T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Undertow, JavaLib.HashIds))
            .tags(Lists.newArrayList(Tags.WebServer, Tags.HTTP))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                             "server",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/parameters/ParametersServer.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(819743073227449306L)
            .title("Logging, gzip,  Blocking, Exception Handling, Metrics, Middleware Chaining in Undertow")
            .metaDesc("Chain / compose middleware for powerful reuseable components. Logging, Blocking, Gzipping, RateLimiting, ACL, Authenticaton, Caching and many more.")
            .dateCreated(LocalDateTime.parse("2017-01-12T20:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-12T20:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Undertow, JavaLib.Jackson, JavaLib.DropwizardMetrics))
            .tags(Lists.newArrayList(Tags.WebServer, Tags.Middleware, Tags.Logging, Tags.ExceptionHandling))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                             "server",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/middleware/MiddlewareServer.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(821046967101159228L)
            .title("Lightweight embedded Java REST server without a Framework")
            .metaDesc("Create your own mini REST framework using Jackson for JSON and Undertow's web server. Handle exceptions, Middleware and reduce code duplication.")
            .dateCreated(LocalDateTime.parse("2017-01-16T12:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-16T12:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Undertow, JavaLib.Jackson, JavaLib.DropwizardMetrics))
            .tags(Lists.newArrayList(Tags.WebServer, Tags.Middleware, Tags.Logging, Tags.ExceptionHandling, Tags.JSON, Tags.REST))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                             "server",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/rest/RestServer.java")
                , FileReference.stubbornJava(
                               "pojo",
                               "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/rest/User.java")
                , FileReference.stubbornJava(
                               "dao",
                               "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/rest/UserDao.java")
                , FileReference.stubbornJava(
                               "requests",
                               "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/rest/UserRequests.java")
                , FileReference.stubbornJava(
                               "routes",
                               "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/rest/UserRoutes.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(821103478735050335L)
            .title("Managing your stack traces")
            .metaDesc("Large frameworks such as Spring and Hibernate and especially when they are combined have notoriously long stack traces. This can lead to perfomance / memory issues (only at certain scales) but mainly debugging hell.")
            .dateCreated(LocalDateTime.parse("2017-01-16T16:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-16T16:15:30"))
            .javaLibs(Lists.newArrayList())
            .tags(Lists.newArrayList(Tags.ExceptionHandling))
            .gitFileReferences(Lists.newArrayList())
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(821159458706815040L)
            .title("HTTP Redirects with Undertow")
            .metaDesc("Handling permanent redirect, temporary redirect and a referrer redirect using Undertow web server.")
            .dateCreated(LocalDateTime.parse("2017-01-16T20:15:30"))
            .dateUpdated(LocalDateTime.parse("2019-03-15T20:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Undertow))
            .tags(Lists.newArrayList(Tags.WebServer, Tags.HTTP))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                     "server",
                     "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/redirects/RedirectServer.java")
                , FileReference.stubbornJava(
                     "redirects",
                     "stubbornjava-undertow/src/main/java/com/stubbornjava/undertow/exchange/RedirectSenders.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(821921404858442503L)
            .title("Monitoring your JVM with Dropwizard Metrics")
            .metaDesc("Monitor JVM stats, garbage collection, memory, threads, logging level hit rates and db connection pool sats with Dropwizard Metrics.")
            .dateCreated(LocalDateTime.parse("2017-01-18T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-18T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.DropwizardMetrics))
            .tags(Lists.newArrayList(Tags.Monitoring))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                             "metrics",
                             "stubbornjava-common/src/main/java/com/stubbornjava/common/Metrics.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(824060980448389752L)
            .title("Java HTML Templating with Handlebars and Undertow")
            .metaDesc("Java handlebars example. HTML Templating using Handlebars templates with an embedded Undertow web server.")
            .dateCreated(LocalDateTime.parse("2017-01-24T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-01-24T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.JknackHandlebars, JavaLib.HtmlCompressor, JavaLib.Undertow))
            .tags(Lists.newArrayList(Tags.HtmlTemplating, Tags.HTTP, Tags.WebServer))
            .gitFileReferences(Lists.newArrayList(
                    FileReference.stubbornJava(
                                 "templating",
                                 "stubbornjava-common/src/main/java/com/stubbornjava/common/Templating.java")
                   , FileReference.stubbornJava(
                                  "handlers",
                                  "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/handlebars/HandlebarsHandlers.java")
                   , FileReference.stubbornJava(
                                  "server",
                                  "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/handlebars/HandlebarsServer.java")
                   , FileReference.stubbornJava(
                                  "sender",
                                  "stubbornjava-common/src/main/java/com/stubbornjava/common/undertow/HtmlTemplateSender.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(828249368119254250L)
            .title("Logging in Java with SLF4J and Logback")
            .metaDesc("Simple synchronous and asynchronous logback.xml logging configurations using a ConsoleAppender in Logback with SLF4J.")
            .dateCreated(LocalDateTime.parse("2017-02-05T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-02-05T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.SLF4J, JavaLib.Logback))
            .tags(Lists.newArrayList(Tags.Logging, Tags.Monitoring))
            .gitFileReferences(Lists.newArrayList(
                    FileReference.stubbornJava(
                                 "logback-xml",
                                 "stubbornjava-examples/src/main/resources/logback.xml")
                    , FileReference.stubbornJava(
                                   "logback",
                                   "stubbornjava-examples/src/main/java/com/stubbornjava/examples/logback/LogbackExamples.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(828947285323181323L)
            .title("Database Connection Pooling in Java with HikariCP")
            .metaDesc("Improve performance and reliability by switching to HikariCP's connection pool. HikariCP example with multiple pools.")
            .dateCreated(LocalDateTime.parse("2017-02-08T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-02-08T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.TypesafeConfig, JavaLib.HikariCP, JavaLib.DropwizardMetrics))
            .tags(Lists.newArrayList(Tags.Logging, Tags.SQL, Tags.Monitoring))
            .gitFileReferences(Lists.newArrayList(
                    FileReference.stubbornJava(
                                 "config",
                                 "stubbornjava-examples/src/main/resources/examples/hikaricp/pools.conf")
                    , FileReference.stubbornJava(
                                   "poolFactory",
                                   "stubbornjava-common/src/main/java/com/stubbornjava/common/db/ConnectionPool.java")
                    , FileReference.stubbornJava(
                                   "pools",
                                   "stubbornjava-examples/src/main/java/com/stubbornjava/examples/hikaricp/ConnectionPools.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(833715638134451294L)
            .title("Multi-project builds with Gradle and Fat JARs with Shadow")
            .metaDesc("Multi-project gradle examples with executable fat jars, shared dependencies and version conflict resolution.")
            .dateCreated(LocalDateTime.parse("2017-02-20T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-02-20T10:15:30"))
            .javaLibs(Lists.newArrayList())
            .tags(Lists.newArrayList(Tags.Gradle))
            .gitFileReferences(Lists.newArrayList(
                    FileReference.stubbornJava(
                                 "parent-settings",
                                 "settings.gradle")
                    , FileReference.stubbornJava(
                                   "parent-build",
                                   "build.gradle")
                    , FileReference.stubbornJava(
                                   "parent-dependencies",
                                   "gradle/dependencies.gradle")
                    , FileReference.stubbornJava(
                                   "common-build",
                                   "stubbornjava-common/build.gradle")
                    , FileReference.stubbornJava(
                                   "undertow-build",
                                   "stubbornjava-undertow/build.gradle")
                    , FileReference.stubbornJava(
                                   "examples-build",
                                   "stubbornjava-examples/build.gradle")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(834409923388349418L)
            .title("Java Enum Lookup by Name or Field Without Throwing Exceptions")
            .metaDesc("Java enumEnum lookup by name without using Enum.valueOf() by utilizing custom methods and Google's Guava. Ignore Enum.valueOf() exception.")
            .dateCreated(LocalDateTime.parse("2017-02-22T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-02-22T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Guava, JavaLib.Jackson))
            .tags(Lists.newArrayList(Tags.Enums))
            .gitFileReferences(Lists.newArrayList(
                    FileReference.stubbornJava(
                                 "enum-utils",
                                 "stubbornjava-common/src/main/java/com/stubbornjava/common/EnumUtils.java")
                    , FileReference.stubbornJava(
                                   "example",
                                   "stubbornjava-examples/src/main/java/com/stubbornjava/examples/common/EnumLookup.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(835530884199761568L)
            .title("Reading File Resources with Guava")
            .metaDesc("Example loading file resources from the classpath as a String or BufferedReader with Guava.")
            .dateCreated(LocalDateTime.parse("2017-02-25T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-02-25T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.Guava))
            .tags(Lists.newArrayList())
            .gitFileReferences(Lists.newArrayList(
                    FileReference.stubbornJava(
                                 "resources",
                                 "stubbornjava-common/src/main/java/com/stubbornjava/common/Resources.java")
                    , FileReference.stubbornJava(
                                   "tests",
                                   "stubbornjava-common/src/test/java/com/stubbornjava/common/ResourcesTest.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(838881832916503030L)
            .title("Monitoring your Java Services with Dropwizard Health Checks")
            .metaDesc("Create custom healtchecks with Dropwizard health checks to monitor your external dependencies.")
            .dateCreated(LocalDateTime.parse("2017-03-06T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-03-06T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.DropwizardMetrics, JavaLib.HikariCP, JavaLib.OkHttp))
            .tags(Lists.newArrayList(Tags.Monitoring))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                             "server",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/healthchecks/HealthChecksServer.java")
                , FileReference.stubbornJava(
                             "check",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/healthchecks/ExternalServiceHealthCheck.java")
                , FileReference.stubbornJava(
                             "handlers",
                             "stubbornjava-common/src/main/java/com/stubbornjava/common/undertow/handlers/CustomHandlers.java")
                , FileReference.stubbornJava(
                             "registry",
                             "stubbornjava-common/src/main/java/com/stubbornjava/common/HealthChecks.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
            .postId(839537141657600766L)
            .title("OkHttpClient Logging Configuration with Interceptors")
            .metaDesc("Enable debug logging with OkHttp using interceptors with various logging levels. OkHttpClient logging example.")
            .dateCreated(LocalDateTime.parse("2017-03-08T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-03-08T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.SLF4J, JavaLib.OkHttp))
            .tags(Lists.newArrayList(Tags.Logging))
            .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                             "client",
                             "stubbornjava-common/src/main/java/com/stubbornjava/common/HttpClient.java")
                , FileReference.stubbornJava(
                             "server",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/okhttp/OkHttpExampleServer.java")
                , FileReference.stubbornJava(
                             "example",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/okhttp/OkHttpLogging.java")
             ))
            .build()
        );
        posts.add(PostRaw.builder()
             .postId(843506452425765194L)
             .title("Obfuscating and Shortening Sequential ids with HashIds")
             .metaDesc("Obfuscate and shorten sequential ids (auto incremented) for shorter urls that are more difficult to guess.")
             .dateCreated(LocalDateTime.parse("2017-03-19T10:15:30"))
             .dateUpdated(LocalDateTime.parse("2017-03-19T10:15:30"))
             .javaLibs(Lists.newArrayList(JavaLib.HashIds, JavaLib.Undertow))
             .tags(Lists.newArrayList())
             .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                             "hashids",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/common/HashIds.java")
                , FileReference.stubbornJava(
                               "server",
                               "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/parameters/ParametersServer.java")
             ))
             .build()
        );
        posts.add(PostRaw.builder()
             .postId(844246265052565495L)
             .title("OkHttp Example REST Client")
             .metaDesc("Creating a custom web service HTTP client using OkHttp to interact with an Undertow web server.")
             .dateCreated(LocalDateTime.parse("2017-03-21T10:15:30"))
             .dateUpdated(LocalDateTime.parse("2017-03-21T10:15:30"))
             .javaLibs(Lists.newArrayList(JavaLib.OkHttp, JavaLib.jOOλ))
             .tags(Lists.newArrayList(Tags.REST, Tags.JSON))
             .gitFileReferences(Lists.newArrayList(
                FileReference.stubbornJava(
                             "client",
                             "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/rest/RestClient.java")
             ))
             .build()
       );
       posts.add(PostRaw.builder()
            .postId(855022528826446171L)
            .title("Virtual Hosting in Undertow's Embedded Java Web Server")
            .metaDesc("Redirect non www to www in undertow using virtual hosting. Virtual hosting example in undertow.")
            .dateCreated(LocalDateTime.parse("2017-04-20T10:15:30"))
            .dateUpdated(LocalDateTime.parse("2017-04-20T10:15:30"))
            .javaLibs(Lists.newArrayList(JavaLib.OkHttp, JavaLib.Undertow))
            .tags(Lists.newArrayList(Tags.WebServer, Tags.HTTP))
            .gitFileReferences(Lists.newArrayList(
               FileReference.stubbornJava(
                            "url",
                            "stubbornjava-common/src/main/java/com/stubbornjava/common/undertow/Urls.java")
               , FileReference.stubbornJava(
                              "server",
                              "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/redirects/VirtualHostServer.java")
            ))
            .build()
       );
       posts.add(PostRaw.builder()
           .postId(876957148739027529L)
           .title("Webpack and npm for Simple Java 8 Web Apps")
           .metaDesc("Server side rendered HTML Java web servers utilizing webpack and npm.")
           .dateCreated(LocalDateTime.parse("2017-06-19T10:15:30"))
           .dateUpdated(LocalDateTime.parse("2017-06-19T10:15:30"))
           .javaLibs(Lists.newArrayList(JavaLib.JknackHandlebars, JavaLib.Undertow))
           .tags(Lists.newArrayList(Tags.WebServer, Tags.HTTP, Tags.NPM, Tags.Webpack, Tags.HtmlTemplating))
           .gitFileReferences(Lists.newArrayList(
              FileReference.stubbornJava(
                  "webpack",
                  "stubbornjava-examples/ui/webpack.config.js")
              , FileReference.stubbornJava(
                  "npm",
                  "stubbornjava-examples/ui/package.json")
              , FileReference.stubbornJava(
                  "java",
                  "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/handlebars/WebpackServer.java")
              , FileReference.stubbornJava(
                  "appjs",
                  "stubbornjava-examples/ui/src/app.js")
              , FileReference.stubbornJava(
                  "layout",
                  "stubbornjava-examples/ui/src/layout.hbs")
              , FileReference.stubbornJava(
                  "hello",
                  "stubbornjava-examples/ui/src/hello.hbs")
           ))
           .build()
      );
      posts.add(PostRaw.builder()
           .postId(877677369351173167L)
           .title("OkHttpClient Trust All SSL Certificates")
           .metaDesc("OkHttpClient example to ignore SSL certificates and errors. Ignore PKIX path building failed in OkHttpClient.")
           .dateCreated(LocalDateTime.parse("2017-06-21T10:15:30"))
           .dateUpdated(LocalDateTime.parse("2017-06-21T10:15:30"))
           .javaLibs(Lists.newArrayList(JavaLib.OkHttp))
           .tags(Lists.newArrayList(Tags.HTTP))
           .gitFileReferences(Lists.newArrayList(
              FileReference.stubbornJava(
                   "httpClient",
                   "stubbornjava-common/src/main/java/com/stubbornjava/common/HttpClient.java")
           ))
           .build()
      );
      posts.add(PostRaw.builder()
           .postId(877736688294485954L)
           .title("Web Scraping in Java using jsoup and OkHttp")
           .metaDesc("jsoup example scraping the DOM using CSS selectors and OkHttpClient for networking.")
           .dateCreated(LocalDateTime.parse("2017-06-22T10:15:30"))
           .dateUpdated(LocalDateTime.parse("2017-06-22T10:15:30"))
           .javaLibs(Lists.newArrayList(JavaLib.OkHttp, JavaLib.jsoup, JavaLib.Failsafe, JavaLib.jOOλ, JavaLib.Guava))
           .tags(Lists.newArrayList(Tags.HTTP, Tags.HtmlTemplating))
           .gitFileReferences(Lists.newArrayList(
               FileReference.stubbornJava(
                  "model",
                  "stubbornjava-webapp/src/main/java/com/stubbornjava/webapp/themes/HtmlCssTheme.java")
               , FileReference.stubbornJava(
                   "scraper",
                   "stubbornjava-webapp/src/main/java/com/stubbornjava/webapp/themes/WrapBootstrapScraper.java")
               , FileReference.stubbornJava(
                   "service",
                   "stubbornjava-webapp/src/main/java/com/stubbornjava/webapp/themes/Themes.java")
               , FileReference.stubbornJava(
                   "routes",
                   "stubbornjava-webapp/src/main/java/com/stubbornjava/webapp/themes/ThemeRoutes.java")
           ))
           .build()
     );
      posts.add(PostRaw.builder()
          .postId(880770018810625706L)
          .title("Creating XML sitemaps in Java")
          .metaDesc("Example XML sitemap in Java using sitemapgen4j to increase page exposure and SEO.")
          .dateCreated(LocalDateTime.parse("2017-07-15T10:15:30"))
          .dateUpdated(LocalDateTime.parse("2017-07-15T10:15:30"))
          .javaLibs(Lists.newArrayList(JavaLib.Undertow, JavaLib.SitemapGen4j))
          .tags(Lists.newArrayList(Tags.SEO))
          .gitFileReferences(Lists.newArrayList(
              FileReference.stubbornJava(
                 "inMemorySitemap",
                 "stubbornjava-common/src/main/java/com/stubbornjava/common/seo/InMemorySitemap.java")
              , FileReference.stubbornJava(
                  "routes",
                  "stubbornjava-common/src/main/java/com/stubbornjava/common/seo/SitemapRoutes.java")
              , FileReference.stubbornJava(
                  "sitemapGen",
                  "stubbornjava-webapp/src/main/java/com/stubbornjava/webapp/StubbornJavaSitemapGenerator.java")
              , FileReference.stubbornJava(
                  "robots",
                  "stubbornjava-webapp/src/main/java/com/stubbornjava/webapp/PageRoutes.java")
          ))
          .build()
    );
      posts.add(PostRaw.builder()
          .postId(892208155060669739L)
          .title("Hashing Passwords in Java with BCrypt")
          .metaDesc("Java BCrypt example using jbcrypt with auto updating salted hashes. Secure password salted hashing in Java with BCrypt.")
          .dateCreated(LocalDateTime.parse("2017-08-01T01:15:30"))
          .dateUpdated(LocalDateTime.parse("2017-08-01T01:15:30"))
          .javaLibs(Lists.newArrayList(JavaLib.jBCrypt, JavaLib.Logback))
          .tags(Lists.newArrayList())
          .gitFileReferences(Lists.newArrayList(
              FileReference.stubbornJava(
                 "hashing",
                 "stubbornjava-common/src/main/java/com/stubbornjava/common/Hashing.java")
              , FileReference.stubbornJava(
                  "bcrypt",
                  "stubbornjava-common/src/main/java/com/stubbornjava/common/UpdatableBCrypt.java")
          ))
          .build()
        );
      posts.add(PostRaw.builder()
          .postId(897427147384949867L)
          .title("Typesafe Config Features and Example Usage")
          .metaDesc("Using Typesafe Config fallback handlers, lists, memory helper, duration helper and configuration resolution.")
          .dateCreated(LocalDateTime.parse("2017-08-15T01:15:30"))
          .dateUpdated(LocalDateTime.parse("2017-08-15T01:15:30"))
          .javaLibs(Lists.newArrayList(JavaLib.TypesafeConfig, JavaLib.Logback))
          .tags(Lists.newArrayList(Tags.Configuration))
          .gitFileReferences(Lists.newArrayList(
              FileReference.stubbornJava(
                 "config",
                 "stubbornjava-examples/src/main/java/com/stubbornjava/examples/common/TypesafeConfigExamples.java")
              , FileReference.stubbornJava(
                  "defaults",
                  "stubbornjava-examples/src/main/resources/defaults.conf")
              , FileReference.stubbornJava(
                  "overrides",
                  "stubbornjava-examples/src/main/resources/overrides.conf")
              ))
          .build()
        );
          posts.add(PostRaw.builder()
              .postId(897667693920523259L)
              .title("Sharing routes and running multiple Java services in a single JVM with Undertow")
              .metaDesc("Build out microservice ready codebases but deploy as a monolith. Don't ride the hype train.")
              .dateCreated(LocalDateTime.parse("2017-09-04T01:15:30"))
              .dateUpdated(LocalDateTime.parse("2017-09-04T01:15:30"))
              .javaLibs(Lists.newArrayList(JavaLib.Undertow))
              .tags(Lists.newArrayList(Tags.Microservice, Tags.Monolith))
              .gitFileReferences(Lists.newArrayList(
                  FileReference.stubbornJava(
                     "services",
                     "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/MicroMonolith.java")
                  ))
              .build()
            );
          posts.add(PostRaw.builder()
              .postId(905411988100628470L)
              .title("Lazy loading and caching objects in Java with Guava's Suppliers.memoize")
              .metaDesc("Use Guava's Suppliers.memoize to lazy load and cache objects. Use Suppliers.memoizeWithExpiration to have an expireable cached object in Java.")
              .dateCreated(LocalDateTime.parse("2017-09-06T01:15:30"))
              .dateUpdated(LocalDateTime.parse("2017-09-06T01:15:30"))
              .javaLibs(Lists.newArrayList(JavaLib.Guava))
              .tags(Lists.newArrayList(Tags.Caching))
              .gitFileReferences(Lists.newArrayList(
                  FileReference.stubbornJava(
                     "suppliers",
                     "stubbornjava-examples/src/main/java/com/stubbornjava/examples/common/SuppliersExamples.java")
                  ))
              .build()
            );
          posts.add(PostRaw.builder()
              .postId(1L)
              .title("Creating a somewhat deterministic Jackson ObjectMapper")
              .metaDesc("Example determinsitic ObjectMapper that can be useful for md5 sums or diffing.")
              .dateCreated(LocalDateTime.parse("2017-09-26T01:15:30"))
              .dateUpdated(LocalDateTime.parse("2017-09-26T01:15:30"))
              .javaLibs(Lists.newArrayList(JavaLib.Jackson, JavaLib.jOOλ))
              .tags(Lists.newArrayList(Tags.JSON))
              .gitFileReferences(Lists.newArrayList(
                  FileReference.stubbornJava(
                     "deterministicMapperTest",
                     "stubbornjava-common/src/test/java/com/stubbornjava/common/DeterministicObjectMapperTest.java")
                  , FileReference.stubbornJava(
                      "deterministicMapper",
                      "stubbornjava-common/src/main/java/com/stubbornjava/common/DeterministicObjectMapper.java")
                  ))
              .build()
            );
          posts.add(PostRaw.builder()
              .postId(2L)
              .title("Creating a local development environment with Docker Compose")
              .metaDesc("Setting up MySQL and Elasticsearch locally with Docker Compose to simplify your development environment. Docker compose mysql example and docker compose elasticsearch example.")
              .dateCreated(LocalDateTime.parse("2017-10-24T01:15:30"))
              .dateUpdated(LocalDateTime.parse("2017-10-24T01:15:30"))
              .javaLibs(Lists.newArrayList())
              .tags(Lists.newArrayList(Tags.Docker, Tags.MySQL, Tags.Elasticsearch))
              .gitFileReferences(Lists.newArrayList(
                  FileReference.stubbornJava(
                     "dockerCompose",
                     "docker/docker-compose.yml")
                  , FileReference.stubbornJava(
                     "mysql",
                     "docker/mysql/Dockerfile")
                  , FileReference.stubbornJava(
                     "mysqlcnf",
                     "docker/mysql/mysqld.cnf")
                  , FileReference.stubbornJava(
                     "mysqlSetup",
                     "docker/mysql/setup.sh")
                  ))
              .build()
            );
          posts.add(PostRaw.builder()
              .postId(3L)
              .title("Installing Java, supervisord, and other service dependencies with Ansible")
              .metaDesc("Example using Ansible to install java8, supervisord, users, groups and file structures in a repeatable way. Configuring AWS EC2 instances with Ansible example.")
              .dateCreated(LocalDateTime.parse("2017-11-17T01:15:30"))
              .dateUpdated(LocalDateTime.parse("2017-11-17T01:15:30"))
              .javaLibs(Lists.newArrayList())
              .tags(Lists.newArrayList(Tags.Ansible, Tags.Supervisord))
              .gitFileReferences(Lists.newArrayList(
                  FileReference.stubbornJava(
                      "serverPlaybook",
                      "ansible/stubbornjava.yml")
                  , FileReference.stubbornJava(
                      "hosts",
                      "ansible/inventories/production/hosts")
                  , FileReference.stubbornJava(
                      "appBaseTasks",
                      "ansible/roles/apps/jvm_app_base/tasks/main.yml")
                  , FileReference.stubbornJava(
                      "appBaseVars",
                      "ansible/roles/apps/jvm_app_base/vars/main.yml")
                  , FileReference.stubbornJava(
                      "appBaseTemplatesSupervisord",
                      "ansible/roles/apps/jvm_app_base/templates/supervisorapp.conf.j2")
                  , FileReference.stubbornJava(
                      "appBaseTemplatesConf",
                      "ansible/roles/apps/jvm_app_base/templates/secure.conf.j2")
                  , FileReference.stubbornJava(
                      "supervisord",
                      "ansible/roles/supervisord/tasks/main.yml")
                  , FileReference.stubbornJava(
                      "supervisordHandlers",
                      "ansible/roles/supervisord/handlers/main.yml")
                  ))
              .build()
            );
          posts.add(PostRaw.builder()
              .postId(4L)
              .title("Configuring Security Headers in Undertow")
              .metaDesc("Control iFrame options, referer header options XSS protection, HSTS, and content type options in Underow.")
              .dateCreated(LocalDateTime.parse("2018-01-17T01:15:30"))
              .dateUpdated(LocalDateTime.parse("2018-01-17T01:15:30"))
              .javaLibs(Lists.newArrayList(JavaLib.Undertow))
              .tags(Lists.newArrayList(Tags.WebServer, Tags.Security, Tags.Middleware))
              .gitFileReferences(Lists.newArrayList(
                  FileReference.stubbornJava(
                      "webserver",
                      "stubbornjava-webapp/src/main/java/com/stubbornjava/webapp/StubbornJavaWebApp.java")
                  , FileReference.stubbornJava(
                      "cspHandler",
                      "stubbornjava-undertow/src/main/java/com/stubbornjava/undertow/handlers/ContentSecurityPolicyHandler.java")
                  , FileReference.stubbornJava(
                      "securityHeadersHandler",
                      "stubbornjava-common/src/main/java/com/stubbornjava/common/undertow/handlers/CustomHandlers.java")
                  , FileReference.stubbornJava(
                      "referrerHandler",
                      "stubbornjava-undertow/src/main/java/com/stubbornjava/undertow/handlers/ReferrerPolicyHandlers.java")
                  , FileReference.stubbornJava(
                      "hstsHandler",
                      "stubbornjava-undertow/src/main/java/com/stubbornjava/undertow/handlers/StrictTransportSecurityHandlers.java")
                  , FileReference.stubbornJava(
                      "contentTypeHandler",
                      "stubbornjava-undertow/src/main/java/com/stubbornjava/undertow/handlers/XContentTypeOptionsHandler.java")
                  , FileReference.stubbornJava(
                      "iframeHandler",
                      "stubbornjava-undertow/src/main/java/com/stubbornjava/undertow/handlers/XFrameOptionsHandlers.java")
                  , FileReference.stubbornJava(
                      "xssHandler",
                      "stubbornjava-undertow/src/main/java/com/stubbornjava/undertow/handlers/XXssProtectionHandlers.java")
                  ))
              .build()
            );
          posts.add(PostRaw.builder()
              .postId(5L)
              .title("Increasing Resiliency with Circuit Breakers in your Undertow Web Server with Failsafe")
              .metaDesc("Utilize circuit breakers to fail fast and recover quickly with a CircuitBreakerHandler in Undertow. Shutoff misbehaving endpoints to allow other endpoints to proceede normally.")
              .dateCreated(LocalDateTime.parse("2018-02-05T01:15:30"))
              .dateUpdated(LocalDateTime.parse("2018-02-05T01:15:30"))
              .javaLibs(Lists.newArrayList(JavaLib.Undertow, JavaLib.Failsafe, JavaLib.OkHttp))
              .tags(Lists.newArrayList(Tags.WebServer, Tags.Resiliency))
              .gitFileReferences(Lists.newArrayList(
                  FileReference.stubbornJava(
                      "handler",
                      "stubbornjava-common/src/main/java/com/stubbornjava/common/undertow/handlers/CircuitBreakerHandler.java")
                  , FileReference.stubbornJava(
                      "example",
                      "stubbornjava-examples/src/main/java/com/stubbornjava/examples/failsafe/FailsafeWebserver.java")
                  ))
              .build()
            );
          posts.add(PostRaw.builder()
              .postId(6L)
              .title("Grafana Cloud Dropwizard Metrics Reporter")
              .metaDesc("Dropwizard Metrics reporter for hosted graphite metrics from Grafana's cloud offering that features hosted Graphite and Prometheus.")
              .dateCreated(LocalDateTime.parse("2019-01-01T01:15:30"))
              .dateUpdated(LocalDateTime.parse("2019-01-01T01:15:30"))
              .javaLibs(Lists.newArrayList(JavaLib.DropwizardMetrics, JavaLib.OkHttp, JavaLib.Jackson))
              .tags(Lists.newArrayList(Tags.Monitoring))
              .gitFileReferences(Lists.newArrayList(
                  FileReference.stubbornJava(
                      "reporters",
                      "stubbornjava-common/src/main/java/com/stubbornjava/common/MetricsReporters.java")
                  , FileReference.stubbornJava(
                      "sender",
                      "stubbornjava-common/src/main/java/com/stubbornjava/common/GraphiteHttpSender.java")
                  ))
              .build()
            );
          posts.add(PostRaw.builder()
              .postId(7L)
              .title("Creating a non-blocking delay in the Undertow Web Server for Artificial Latency")
              .metaDesc("Adding atrificial latency to Undertow HTTP routes for testing / diagnostics by using a non blocking sleep.")
              .dateCreated(LocalDateTime.parse("2019-03-13T01:15:30"))
              .dateUpdated(LocalDateTime.parse("2019-03-13T01:15:30"))
              .javaLibs(Lists.newArrayList(JavaLib.Undertow, JavaLib.OkHttp, JavaLib.Guava))
              .tags(Lists.newArrayList(Tags.HTTP, Tags.Middleware))
              .gitFileReferences(Lists.newArrayList(
                  FileReference.stubbornJava(
                      "delayedHandler",
                      "stubbornjava-common/src/main/java/com/stubbornjava/common/undertow/handlers/diagnostic/DelayedExecutionHandler.java")
                  , FileReference.stubbornJava(
                      "diagnostic",
                      "stubbornjava-common/src/main/java/com/stubbornjava/common/undertow/handlers/diagnostic/DiagnosticHandlers.java")
                  , FileReference.stubbornJava(
                      "http",
                      "stubbornjava-common/src/main/java/com/stubbornjava/common/Http.java")
                  , FileReference.stubbornJava(
                      "example",
                      "stubbornjava-examples/src/main/java/com/stubbornjava/examples/undertow/handlers/DelayedHandlerExample.java")
                  ))
              .build()
            );
    }

    public static List<PostRaw> getPosts() {
        return posts;
    }
}
