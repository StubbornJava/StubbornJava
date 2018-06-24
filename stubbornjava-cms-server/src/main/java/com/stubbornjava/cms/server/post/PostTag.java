package com.stubbornjava.cms.server.post;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
@AllArgsConstructor
public class PostTag {
    private final Integer postTagId;
    private final Integer appId;
    private final String name;
    private final LocalDateTime lastUpdateTs;
}

