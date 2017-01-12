package com.stubbornjava.common.undertow;

import com.stubbornjava.undertow.exchange.ContentTypeSenders;
import com.stubbornjava.undertow.exchange.RedirectSenders;

/*
 * Using static globals for simplicity. Use your own DI method however you want.
 */
public class Exchange {
    public static interface BodyImpl extends ContentTypeSenders {};
    private static final BodyImpl BODY = new BodyImpl(){};
    public static BodyImpl body() {
        return BODY;
    }

    public static interface RedirectImpl extends RedirectSenders {};
    private static final RedirectImpl REDIRECT = new RedirectImpl(){};
    public static RedirectImpl redirect() {
        return REDIRECT;
    }

    public static interface QueryParamImpl extends RedirectSenders {};
    private static final QueryParamImpl QUERYPARAMS = new QueryParamImpl(){};
    public static QueryParamImpl queryParams() {
        return QUERYPARAMS;
    }
}
