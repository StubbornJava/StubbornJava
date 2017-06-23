package com.stubbornjava.webapp.post;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class Post {
    @JsonUnwrapped
    private final PostMeta postMeta;
    private final String content;
}
