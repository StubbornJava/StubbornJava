package com.stubbornjava.webapp.post;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.stubbornjava.cms.server.post.PostMeta;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class Post {
    @JsonUnwrapped
    private final PostMeta postMeta;
    private final String content;
    private final String contentTemplate;
}
