# Custom Undertow HttpHandlers

## Middleware (Filters / interceptors)
The MiddlewareBuilder is a helper for composing HttpHandlers. Many existing and custom HttpHandlers are passed one or more delegate handlers and route accordingly based on some logic. Maintaining deeply nested handlers can sometimes be a challenge. [Middlware](https://www.stubbornjava.com/posts/logging-gzip-blocking-exception-handling-metrics-middleware-chaining-in-undertow) is here to help.
```
MiddlewareBuilder.begin(BlockingHandler::new)
                 .next(CustomHandlers::gzip)
                 .next(CustomHandlers::accessLog)
                 .next(CustomHandlers::statusCodeMetrics)
                 .next(MiddlewareServer::exceptionHandler)
                 .complete(handler);
```
                            
