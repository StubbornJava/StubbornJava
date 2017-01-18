# Exchange Utils
A set of common utility methods for interacting with [HttpServerExchange](https://github.com/undertow-io/undertow/blob/master/core/src/main/java/io/undertow/server/HttpServerExchange.java). Generally the helpers are simple functions that interact with two or more attributes that frequently go together. Example sending a content-type and body or sending a temporary redirect to the referring url.

## Content Types
* [Handling common content types](https://www.stubbornjava.com/posts/handling-content-types-with-undertow)
* [Query and Path Parameters](https://www.stubbornjava.com/posts/query-parameters-and-path-parameters-in-undertow) (`/demo/{path}?query=param`)

## Redirects (301, 302)
Simple 301 and 302 redirects in Undertow as well as a useful [Referral Redirect in Undertow](https://www.stubbornjava.com/posts/http-redirects-with-undertow#redirects)
