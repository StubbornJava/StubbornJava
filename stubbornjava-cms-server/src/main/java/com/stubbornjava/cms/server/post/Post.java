package com.stubbornjava.cms.server.post;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder=true)
@AllArgsConstructor
public class Post {
    private final Long postId;
    private final Integer appId;
    private final String title;
    private final String slug;
    private final String metadesc;
    private final String draftStatus;
    private final LocalDateTime lastUpdateTs;
    private final LocalDateTime dateCreatedTs;
    private final LocalDate dateCreated;
    private final String contentTemplate;
}
