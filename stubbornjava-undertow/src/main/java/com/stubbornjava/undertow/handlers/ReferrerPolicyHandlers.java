package com.stubbornjava.undertow.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.SetHeaderHandler;

// {{start:handler}}
public class ReferrerPolicyHandlers {
    private static final String REFERRER_POLICY_STRING = "Referrer-Policy";

    // See https://scotthelme.co.uk/a-new-security-header-referrer-policy/
    public enum ReferrerPolicy {
        EMPTY(""),
        NO_REFERRER("no-referrer"),
        NO_REFERRER_WHEN_DOWNGRADE("no-referrer-when-downgrade"),
        SAME_ORIGIN("same-origin"),
        ORIGIN("origin"),
        STRICT_ORIGIN("strict-origin"),
        ORIGIN_WHEN_CROSS_ORIGIN("origin-when-cross-origin"),
        STRICT_ORIGIN_WHEN_CROSS_ORIGIN("strict-origin-when-cross-origin"),
        UNSAFE_URL("unsafe-url");

        private final String value;
        ReferrerPolicy(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    };
    public static HttpHandler policy(HttpHandler next, ReferrerPolicy policy) {
        return new SetHeaderHandler(next, REFERRER_POLICY_STRING, policy.getValue());
    }
}
// {{end:handler}}
