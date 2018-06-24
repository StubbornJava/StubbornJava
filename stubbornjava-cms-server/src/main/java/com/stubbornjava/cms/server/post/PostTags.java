package com.stubbornjava.cms.server.post;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.lambda.Seq;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.stubbornjava.cms.server.CMSBootstrap;
import com.stubbornjava.cms.server.CmsDSLs;
import com.stubbornjava.cms.server.generated.Tables;
import com.stubbornjava.cms.server.generated.tables.PostTable;
import com.stubbornjava.cms.server.generated.tables.PostTagLinksTable;
import com.stubbornjava.cms.server.generated.tables.PostTagTable;
import com.stubbornjava.cms.server.generated.tables.records.PostTagLinksRecord;
import com.stubbornjava.cms.server.generated.tables.records.PostTagRecord;
import com.stubbornjava.common.Multimaps;
import com.stubbornjava.common.db.Dao;

public class PostTags {
    private PostTags() {}
    private static final PostTable post = Tables.POST;
    private static final PostTagTable postTag = Tables.POST_TAG;
    private static final PostTagLinksTable postTagLinks = Tables.POST_TAG_LINKS;

    private static final Dao<PostTagRecord, PostTag, PostTagTable> postTagDao = new Dao<>(Tables.POST_TAG, PostTags::fromRecord, PostTags::toRecord);

    public static PostTag create(DSLContext ctx, PostTag tag) {
        return postTagDao.insertReturning(ctx, tag);
    }

    public static List<PostTag> findPostTagsByName(DSLContext ctx, int appId, Collection<String> tags) {
        return postTagDao.fetch(ctx, pt -> pt.APP_ID.eq(appId)
                                             .and(pt.NAME.in(tags)));
    }

    public static Multimap<Long, PostTag> findTagsForPosts(DSLContext ctx, int appId, Collection<Long> postIds) {
        List<Field<?>> fields = Lists.newArrayList(postTagLinks.POST_ID);
        fields.addAll(Arrays.asList(postTag.fields()));
        Map<Long, List<PostTag>> tags = ctx.select(fields)
                                .from(postTag)
                                    .join(postTagLinks)
                                    .on(postTag.POST_TAG_ID.eq(postTagLinks.POST_TAG_ID))
                                .where(postTagLinks.POST_ID.in(postIds))
                                .fetchGroups(postTagLinks.POST_ID, record -> {
                                    return PostTags.fromRecord(record.into(postTag));
                                });
        return Multimaps.newListMultimap(tags);
    }

    /*
     * There are some race conditions here if there's two updates
     * at the same time but good enough for now.
     */
    public static void linkTagsToPost(DSLContext ctx, int appId, long postId, List<PostTag> tags) {
        ctx.deleteFrom(Tables.POST_TAG_LINKS)
           .where(Tables.POST_TAG_LINKS.POST_ID.eq(postId));
        List<PostTagLinksRecord> records = Seq.seq(tags)
                                              .map(t -> new PostTagLinksRecord(postId, t.getPostTagId()))
                                              .toList();
        ctx.batchInsert(records).execute();
    }

    public static List<PostTag> getAllTagsForApp(DSLContext ctx, Integer appId) {
        return postTagDao.fetch(ctx, postTag -> postTag.APP_ID.eq(appId));
    }

    static PostTagRecord toRecord(PostTag tag) {
        return new PostTagRecord(tag.getPostTagId(), tag.getAppId(), tag.getName(), tag.getLastUpdateTs());
    }

    static PostTag fromRecord(PostTagRecord record) {
        return new PostTag(record.getPostTagId(), record.getAppId(), record.getName(), record.getLastUpdateTs());
    }

    public static void main(String[] args) {
        CMSBootstrap.run(() -> {
           CmsDSLs.transactional().transaction(ctx -> {
               Multimap<Long, PostTag> results = findTagsForPosts(ctx, 1, Lists.newArrayList(1L, 2L));
               System.out.println();
           });
        });
    }
}
