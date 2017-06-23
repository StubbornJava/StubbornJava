package com.stubbornjava.common.undertow;

import com.stubbornjava.undertow.exchange.ContentTypeSenders;
import com.stubbornjava.undertow.exchange.PathParams;
import com.stubbornjava.undertow.exchange.QueryParams;
import com.stubbornjava.undertow.exchange.RedirectSenders;

/*
 * Using static globals for simplicity. Use your own DI method however you want.
 */
public class Exchange {
    public static interface BodyImpl extends
        ContentTypeSenders
        , JsonSender
        , JsonParser
        , HtmlTemplateSender {};
    private static final BodyImpl BODY = new BodyImpl(){};
    public static BodyImpl body() {
        return BODY;
    }

    public static interface RedirectImpl extends RedirectSenders {};
    private static final RedirectImpl REDIRECT = new RedirectImpl(){};
    public static RedirectImpl redirect() {
        return REDIRECT;
    }

    public static interface QueryParamImpl extends QueryParams {};
    private static final QueryParamImpl QUERYPARAMS = new QueryParamImpl(){};
    public static QueryParamImpl queryParams() {
        return QUERYPARAMS;
    }

    public static interface PathParamImpl extends PathParams {};
    private static final PathParamImpl PATHPARAMS = new PathParamImpl(){};
    public static PathParamImpl pathParams() {
        return PATHPARAMS;
    }

    public static interface UrlImpl extends Urls {};
    private static final UrlImpl URLS = new UrlImpl(){};
    public static UrlImpl urls() {
        return URLS;
    }

    public static interface HeaderImpl extends Headers {};
    private static final HeaderImpl HEADERS = new HeaderImpl(){};
    public static HeaderImpl headers() {
        return HEADERS;
    }

}
