# Custom Undertow HttpHandlers

## MiddlewareBuilder (Filters / interceptors)
The MiddlewareBuilder is a helper for composing HttpHandlers. Many existing and custom HttpHandlers are passed one or more delegate handlers and route accordingly based on some logic. Maintaining deeply nested handlers can sometimes be a challenge. [Undertow MiddlwareBuilder](https://www.stubbornjava.com/posts/logging-gzip-blocking-exception-handling-metrics-middleware-chaining-in-undertow) is here to help.
```
MiddlewareBuilder.begin(BlockingHandler::new)
                 .next(CustomHandlers::gzip)
                 .next(CustomHandlers::accessLog)
                 .next(CustomHandlers::statusCodeMetrics)
                 .next(MiddlewareServer::exceptionHandler)
                 .complete(handler);
```
                            
## Access Logging
Undertow comes with a flexbile access log handler. Creating a custom log receiver is easy.
### Slf4jAccessLogReceiver
Example implementation of [SLF4J Undertow Access Logging](https://www.stubbornjava.com/posts/http-access-logging-with-undertow#adding-the-accessloghandler)
