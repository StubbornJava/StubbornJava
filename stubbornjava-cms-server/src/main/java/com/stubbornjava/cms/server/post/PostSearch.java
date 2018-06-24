package com.stubbornjava.cms.server.post;

import java.util.Set;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder(toBuilder=true)
public class PostSearch {
    private final int appId;
    @Singular private final Set<Long> excludePostIds;
    @Singular private final Set<String> tagNames;
    private final Integer limit;
}
