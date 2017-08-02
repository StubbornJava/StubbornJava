package com.stubbornjava.webapp.post;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jooq.lambda.Seq;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JavaLib {
    Undertow(
       "Undertow"
       , "http://undertow.io/"
       , "https://github.com/undertow-io"
       , "Undertow is a highly performant Java web server which provides both blocking and non-blocking API's based on NIO. The embeddable server is easily configured and also has sane defaults for modern web development. The API is clean and request lifecycle is very straight forward. HttpHandlers are the single purpose building blocks for handling requests and responses and provide you with as much or little flexibility as you need."
    )
    , Jackson(
        "Jackson"
        , "http://wiki.fasterxml.com/JacksonHome"
        , "https://github.com/FasterXML/jackson"
        , "A high performance JSON serialization library with pojo data binding as well as a lower level streaming API."
    )
    , TypesafeConfig(
        "Typesafe Config"
        , "https://typesafehub.github.io/config/"
        , "https://github.com/typesafehub/config"
        , "Typesafe Config is a JVM configuration library with a simple and clean API with a rich feature set."
    )
    , JknackHandlebars(
        "Jknack Handlebars"
        , "http://jknack.github.io/handlebars.java/"
        , "https://github.com/jknack/handlebars.java"
        , "Java port of Handlebars HTML templating (http://handlebarsjs.com/)."
    )
    , HtmlCompressor(
        "htmlcompressor"
        , "https://code.google.com/archive/p/htmlcompressor/"
        , null
        , "Java implementation of a HTML compressor."
    )
    , DropwizardMetrics(
        "Dropwizard Metrics"
        , "http://metrics.dropwizard.io/"
        , "https://github.com/dropwizard/metrics"
        , "Metrics provides a powerful toolkit of ways to measure the behavior of critical components in your production environment"
    )
    , SLF4J(
        "SLF4J"
        , "https://www.slf4j.org/"
        , "https://github.com/qos-ch/slf4j"
        , "The Simple Logging Facade for Java (SLF4J) serves as a simple facade or abstraction for various logging frameworks (e.g. java.util.logging, logback, log4j) allowing the end user to plug in the desired logging framework at deployment time."
    )
    , Logback(
        "Logback"
        , "https://logback.qos.ch/"
        , "https://github.com/qos-ch/logback"
        , "A simple and performant Java logging library which implements SLF4J apis."
    )
    , HikariCP(
        "HikariCP"
        , "http://brettwooldridge.github.io/HikariCP/"
        , "https://github.com/brettwooldridge/HikariCP"
        , "Fast, simple, reliable. HikariCP is a \"zero-overhead\" production ready JDBC connection pool. At roughly 130Kb, the library is very light."
    )
    , Guava(
        "Guava"
        , "https://github.com/google/guava/wiki"
        , "https://github.com/google/guava"
        , "Guava is a set of core libraries that includes new collection types (such as multimap and multiset), immutable collections, a graph library, functional types, an in-memory cache, and APIs/utilities for concurrency, I/O, hashing, primitives, reflection, string processing, and much more!"
    )
    , OkHttp(
        "OkHttp"
        , "http://square.github.io/okhttp/"
        , "https://github.com/square/okhttp"
        , "OkHttp is a modern java HTTP client with sane defaults and built for resiliency. It supports HTTP/2, GZIP, caching, logging, following redirects and much more."
    )
    , HashIds(
        "HashIds"
        , "http://hashids.org/java/"
        , "https://github.com/jiecao-fm/hashids-java"
        , "A small Java class to generate YouTube-like hashes from one or many numbers."
    )
    , jOOλ(
        "jOOλ"
        , "https://www.jooq.org/products/"
        , "https://github.com/jooq/jool"
        , "jOOλ improves the JDK libraries in areas where the Expert Group's focus was elsewhere. It adds tuple support, function support, and a lot of additional functionality around sequential Streams."
    )
    , Flyway(
        "Flyway"
        , "https://flywaydb.org/"
        , "https://github.com/flyway/flyway"
        , "Version control for your database so you can migrate it with ease and confidence."
    )
    , jsoup(
        "jsoup"
        , "https://jsoup.org/"
        , "https://github.com/jhy/jsoup"
        , "jsoup is a Java library for working with real-world HTML. It provides a very convenient API for extracting and manipulating data, using the best of DOM, CSS, and jquery-like methods. jsoup implements the WHATWG HTML5 specification, and parses HTML to the same DOM as modern browsers do."
    )
    , Failsafe(
        "Failsafe"
        , null
        , "https://github.com/jhalterman/failsafe"
        , "Failsafe is a lightweight, zero-dependency library for handling failures. It was designed to be as easy to use as possible, with a concise API for handling everyday use cases and the flexibility to handle everything else. Failsafe "
    )
    , SitemapGen4j(
        "SitemapGen4j",
        null,
        "https://github.com/dfabulich/sitemapgen4j",
        "SitemapGen4j is a library to generate XML sitemaps in Java."
    )
    , jBCrypt(
        "jBCrypt",
        "http://www.mindrot.org/projects/jBCrypt/",
        null,
        "jBCrypt is a Java™ implementation of OpenBSD's Blowfish password hashing code, as described in \"A Future-Adaptable Password Scheme\" by Niels Provos and David Mazières."
    )
    ;

    private final String name;
    private final String url;
    private final String github;
    private final String description;
    private JavaLib(String name, String url, String github, String description) {
        this.name = name;
        this.url = url;
        this.github = github;
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public String getGithub() {
        return github;
    }
    public String getDescription() {
        return description;
    }

    private static final List<JavaLib> sorted = Collections.unmodifiableList(
        Seq.of(JavaLib.values())
           .sorted((lib) -> lib.getName())
           .toList()
    );
    public static List<JavaLib> sorted() {
        return sorted;
    }

    private static final Map<String, JavaLib> nameIndex =
            Seq.of(JavaLib.values())
               .toMap(JavaLib::getName);
    public static final JavaLib findByName(String name) {
        return nameIndex.get(name);
    }
}
