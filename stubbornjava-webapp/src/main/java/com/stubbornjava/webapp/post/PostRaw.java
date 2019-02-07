package com.stubbornjava.webapp.post;

import java.time.LocalDateTime;
import java.util.List;

import com.stubbornjava.common.Slugs;
import com.stubbornjava.webapp.SiteUrls;
import com.stubbornjava.webapp.github.FileReference;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class PostRaw {

    private final long postId;
    private final LocalDateTime dateCreated;
    private final LocalDateTime dateUpdated;
    private final String title;
    private final String metaDesc;
    private final String template;
    @Singular private final List<JavaLib> javaLibs;
    @Singular private final List<Tag> tags;
    @Singular private final List<FileReference> gitFileReferences;

    public String getSlug() {
        return Slugs.toSlug(title);
    }

    public String getUrl() {
        return SiteUrls.postUrl(getSlug());
    }
}
