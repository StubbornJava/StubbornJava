package com.stubbornjava.cms.server.post;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class PostInfo {
    private final long postId;
    private final int appId;
    private final String title;
    private final String slug;
    private final LocalDate dateCreated;
    @Singular private final List<String> tags;
}
