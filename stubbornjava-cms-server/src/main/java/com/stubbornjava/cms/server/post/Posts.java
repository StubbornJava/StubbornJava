package com.stubbornjava.cms.server.post;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.jooq.lambda.Seq;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.stubbornjava.cms.server.generated.Tables;
import com.stubbornjava.cms.server.generated.tables.PostTable;
import com.stubbornjava.cms.server.generated.tables.PostTagLinksTable;
import com.stubbornjava.cms.server.generated.tables.PostTagTable;
import com.stubbornjava.cms.server.generated.tables.records.PostRecord;
import com.stubbornjava.common.db.Dao;

public class Posts {
    private Posts() {}
    private static final PostTable post = Tables.POST;
    private static final PostTagTable postTag = Tables.POST_TAG;
    private static final PostTagLinksTable postTagLinks = Tables.POST_TAG_LINKS;
    private static final Dao<PostRecord, Post, PostTable> postDao = new Dao<>(Tables.POST, Posts::fromRecord, Posts::toRecord);

    private static final List<Field<?>> postMetaFields = Lists.newArrayList(post.POST_ID, post.APP_ID, post.TITLE, post.SLUG, post.DATE_CREATED);

    public static FullPost create(DSLContext ctx, int appId, FullPost fullPost) {
        Post post = postFromFull(fullPost);
        Post created = postDao.insertReturning(ctx, post);
        Set<String> tags = fullPost.getTags();
        List<PostTag> postTags = PostTags.findPostTagsByName(ctx, appId, tags);
        PostTags.linkTagsToPost(ctx, appId, created.getPostId(), postTags);
        return buildFullPost(created, postTags);
    }


    public static List<PostMeta> getRecentPostsExcluding(DSLContext ctx, int appId, Set<Long> excludePostIds) {
        PostSearch search = PostSearch.builder()
             .appId(appId)
             .excludePostIds(excludePostIds)
             .limit(10)
             .build();
        return postSearch(ctx, appId, search);
    }

    public static List<PostMeta> getRecentPosts(DSLContext ctx, int appId) {
        return getRecentPosts(ctx, appId, 10);
    }

    public static List<PostMeta> getRecentPosts(DSLContext ctx, int appId, int num) {
        PostSearch search = PostSearch.builder()
            .appId(appId)
            .limit(num)
            .build();
        return postSearch(ctx, appId, search);
    }

    public static List<PostMeta> getRecentPostsWithTag(DSLContext ctx, int appId, String tag) {
        PostSearch search = PostSearch.builder()
                .appId(appId)
                .tagName(tag)
                .limit(50)
                .build();
        return postSearch(ctx, appId, search);
    }

    private static List<PostMeta> postSearch(DSLContext ctx, int appId, PostSearch searchRequest) {
        Condition condition = DSL.trueCondition();

        if (!searchRequest.getExcludePostIds().isEmpty()) {
            condition.and(post.POST_ID.notIn(searchRequest.getExcludePostIds()));
        }

        if (!searchRequest.getTagNames().isEmpty()) {
            condition.and(post.POST_ID.in(ctx.select(postTagLinks.POST_ID)
                                             .from(postTag)
                                             .where(postTag.NAME.in(searchRequest.getTagNames()))));
        }

        int limit = Optional.ofNullable(searchRequest.getLimit()).orElse(50);

        List<PostMeta> posts = ctx.select(postMetaFields)
                                  .from(post)
                                  .where(condition)
                                  .orderBy(post.DATE_CREATED.desc(), post.DATE_CREATED_TS.desc())
                                  .limit(limit)
                                  .fetch(record -> Posts.metaFromRecord(record.into(post)));
        Multimap<Long, PostTag> tagsByPostId = PostTags.findTagsForPosts(ctx, appId, metaIds(posts));

        List<PostMeta> postMetas = Seq.seq(posts).map(p -> {
            List<String> tagNames = Seq.seq(tagsByPostId.get(p.getPostId())).map(PostTag::getName).toList();
            return p.toBuilder()
                    .tags(tagNames)
                    .build();
        }).toList();
        return postMetas;
    }

    private static Set<Long> ids(Collection<Post> posts) {
        return Seq.seq(posts).map(Post::getPostId).toSet();
    }

    private static Set<Long> metaIds(Collection<PostMeta> posts) {
        return Seq.seq(posts).map(PostMeta::getPostId).toSet();
    }

    private static List<FullPost> buildFullPosts(DSLContext ctx, List<Post> posts, Multimap<Long, PostTag> tagsByPostId) {
        return Seq.seq(posts)
                  .map(post -> buildFullPost(post, tagsByPostId.get(post.getPostId())))
                  .toList();
    }

    private static FullPost buildFullPost(Post post, Collection<PostTag> tags) {
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
            Seq.seq(tags).map(PostTag::getName).toSet());
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

    static PostMeta metaFromRecord(PostRecord record) {
        return new PostMeta(
            record.getPostId(),
            record.getAppId(),
            record.getTitle(),
            record.getSlug(),
            record.getDateCreated(),
            null
            );
    }
}
