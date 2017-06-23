package com.stubbornjava.webapp.post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class PostMeta {
    private final long postId;
    private final String title;
    private final String metaDesc;
    private final String slug;
    @Singular private final List<TagOrLibrary> tagOrLibraries;
    private final LocalDateTime dateCreated;
    public LocalDate dateCreatedShort() {
        return dateCreated.toLocalDate();
    }
}
