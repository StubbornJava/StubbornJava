package com.stubbornjava.cms.server.post;

import java.util.List;
import java.util.Set;

import org.jooq.DSLContext;

import com.stubbornjava.cms.server.generated.Tables;
import com.stubbornjava.cms.server.generated.tables.PostTable;
import com.stubbornjava.cms.server.generated.tables.records.PostRecord;
import com.stubbornjava.common.db.Dao;

public class Posts {
    private Posts() {}
    private static final Dao<PostRecord, Post, PostTable> postDao = new Dao<>(Tables.POST, Posts::fromRecord, Posts::toRecord);

    public static FullPost create(DSLContext ctx, int appId, FullPost fullPost) {
        Post post = postFromFull(fullPost);
        Post created = postDao.insertReturning(ctx, post);
        Set<String> tags = fullPost.getTags();
        List<PostTag> postTags = PostTags.findPostTagsByName(ctx, appId, tags);
        PostTags.linkTagsToPost(ctx, appId, created.getPostId(), postTags);
        return buildFullPost(created);
    }

    static FullPost buildFullPost(Post post) {
        return new FullPost(
                post.getPostId(),
                post.getAppId(),
                post.getTitle(),
                post.getSlug(),
                post.getMetadesc(),
                post.getDraftStatus(),
                post.getLastUpdateTs(),
                post.getDateCreatedTs(),
                post.getDateCreated(),
                post.getContentTemplate(),
                null);
    }

    static Post postFromFull(FullPost fullPost) {
        return new Post(
                fullPost.getPostId(),
                fullPost.getAppId(),
                fullPost.getTitle(),
                fullPost.getSlug(),
                fullPost.getMetadesc(),
                fullPost.getDraftStatus(),
                fullPost.getLastUpdateTs(),
                fullPost.getDateCreatedTs(),
                fullPost.getDateCreated(),
                fullPost.getContentTemplate()
                );
    }

    static PostRecord toRecord(Post post) {
        return new PostRecord(
            post.getPostId(),
            post.getAppId(),
            post.getTitle(),
            post.getSlug(),
            post.getMetadesc(),
            post.getDraftStatus(),
            post.getLastUpdateTs(),
            post.getDateCreatedTs(),
            post.getDateCreated(),
            post.getContentTemplate()
            );
    }

    static Post fromRecord(PostRecord record) {
        return new Post(
            record.getPostId(),
            record.getAppId(),
            record.getTitle(),
            record.getSlug(),
            record.getMetadesc(),
            record.getDraftStatus(),
            record.getLastUpdateTs(),
            record.getDateCreatedTs(),
            record.getDateCreated(),
            record.getContentTemplate()
            );
    }
}
