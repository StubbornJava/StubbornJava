package com.stubbornjava.webapp.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.net.UrlEscapers;

import lombok.Value;

@Value
public class TagOrLibrary {
    private final String name;
    private final Type type;
    private final Integer count;

    public String getPath() {
        return String.format(type.getPathTemplate(), UrlEscapers.urlFragmentEscaper().escape(name));
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum Type {
        Tag("tag", "/tags/%s/posts")
        , Library("lib", "/java-libraries/%s")
        ;

        // doesn't belong here oh well.
        private final String cssName;
        private final String pathTemplate;
        Type(String cssName, String pathTemplate) {
            this.cssName = cssName;
            this.pathTemplate = pathTemplate;
        }
        public String getCssName() {
            return cssName;
        }
        public String getPathTemplate() {
            return pathTemplate;
        }
    }
}
