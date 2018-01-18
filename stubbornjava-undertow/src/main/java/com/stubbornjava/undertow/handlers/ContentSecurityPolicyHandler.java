package com.stubbornjava.undertow.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.SetHeaderHandler;

// {{start:handler}}
public class ContentSecurityPolicyHandler {
    private static final String CSP_HEADER = "Content-Security-Policy";

    public enum ContentSecurityPolicy {
        NONE("'none'"), // blocks the use of this type of resource.
        SELF("'self'"), // matches the current origin (but not subdomains).
        UNSAFE_INLINE("'unsafe-inline'"), // allows the use of inline JS and CSS.
        UNSAFE_EVAL("'unsafe-eval'"), // allows the use of mechanisms like eval().
        ;

        private final String value;
        ContentSecurityPolicy(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    // https://scotthelme.co.uk/content-security-policy-an-introduction/#whatcanweprotect
    public static class Builder {
        private final Map<String, String> policyMap;

        public Builder() {
            this.policyMap = new HashMap<>();
        }

        public Builder defaultSrc(ContentSecurityPolicy policy) {
            policyMap.put("default-src", policy.getValue());
            return this;
        }

        public Builder defaultSrc(String... policies) {
            policyMap.put("default-src", join(policies));
            return this;
        }

        public Builder scriptSrc(ContentSecurityPolicy policy) {
            policyMap.put("script-src", policy.getValue());
            return this;
        }

        public Builder scriptSrc(String... policies) {
            policyMap.put("script-src", join(policies));
            return this;
        }

        public Builder objectSrc(ContentSecurityPolicy policy) {
            policyMap.put("object-src", policy.getValue());
            return this;
        }

        public Builder objectSrc(String... policies) {
            policyMap.put("object-src", join(policies));
            return this;
        }

        public Builder styleSrc(ContentSecurityPolicy policy) {
            policyMap.put("style-src", policy.getValue());
            return this;
        }

        public Builder styleSrc(String... policies) {
            policyMap.put("style-src", join(policies));
            return this;
        }

        public Builder imgSrc(ContentSecurityPolicy policy) {
            policyMap.put("img-src", policy.getValue());
            return this;
        }

        public Builder imgSrc(String... policies) {
            policyMap.put("img-src", join(policies));
            return this;
        }

        public Builder mediaSrc(ContentSecurityPolicy policy) {
            policyMap.put("media-src", policy.getValue());
            return this;
        }

        public Builder mediaSrc(String... policies) {
            policyMap.put("media-src", join(policies));
            return this;
        }

        public Builder frameSrc(ContentSecurityPolicy policy) {
            policyMap.put("frame-src", policy.getValue());
            return this;
        }

        public Builder frameSrc(String... policies) {
            policyMap.put("frame-src", join(policies));
            return this;
        }

        public Builder fontSrc(ContentSecurityPolicy policy) {
            policyMap.put("font-src", policy.getValue());
            return this;
        }

        public Builder fontSrc(String... policies) {
            policyMap.put("font-src", join(policies));
            return this;
        }

        public Builder connectSrc(ContentSecurityPolicy policy) {
            policyMap.put("connect-src", policy.getValue());
            return this;
        }

        public Builder connectSrc(String... policies) {
            policyMap.put("connect-src", join(policies));
            return this;
        }

        public Builder formAction(ContentSecurityPolicy policy) {
            policyMap.put("form-action", policy.getValue());
            return this;
        }

        public Builder formAction(String... policies) {
            policyMap.put("form-action", join(policies));
            return this;
        }

        public Builder sandbox(ContentSecurityPolicy policy) {
            policyMap.put("sandbox", policy.getValue());
            return this;
        }

        public Builder sandbox(String... policies) {
            policyMap.put("sandbox", join(policies));
            return this;
        }

        public Builder scriptNonce(ContentSecurityPolicy policy) {
            policyMap.put("script-nonce", policy.getValue());
            return this;
        }

        public Builder scriptNonce(String... policies) {
            policyMap.put("script-nonce", join(policies));
            return this;
        }

        public Builder pluginTypes(ContentSecurityPolicy policy) {
            policyMap.put("plugin-types", policy.getValue());
            return this;
        }

        public Builder pluginTypes(String... policies) {
            policyMap.put("plugin-types", join(policies));
            return this;
        }

        public Builder reflectedXss(ContentSecurityPolicy policy) {
            policyMap.put("reflected-xss", policy.getValue());
            return this;
        }

        public Builder reflectedXss(String... policies) {
            policyMap.put("reflected-xss", join(policies));
            return this;
        }

        public Builder reportUri(String uri) {
            policyMap.put("report-uri", uri);
            return this;
        }

        public HttpHandler build(HttpHandler delegate) {
            String policy = policyMap.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.joining("; "));
            return new SetHeaderHandler(delegate, CSP_HEADER, policy);
        }

        private String join(String... strings) {
            return Stream.of(strings).collect(Collectors.joining(" "));
        }
    }
}
// {{end:handler}}
