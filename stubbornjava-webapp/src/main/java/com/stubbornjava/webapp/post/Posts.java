package com.stubbornjava.webapp.post;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.jooq.lambda.Seq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.stubbornjava.cms.server.CmsDSLs;
import com.stubbornjava.cms.server.post.DraftStatus;
import com.stubbornjava.cms.server.post.PostInfo;
import com.stubbornjava.common.Resources;
import com.stubbornjava.common.Templating;
import com.stubbornjava.webapp.WebappBoostrap;
import com.stubbornjava.webapp.github.FileContent;
import com.stubbornjava.webapp.github.GitHubSource;
import com.stubbornjava.webapp.post.TagOrLibrary.Type;

public class Posts {
    private static final Logger logger = LoggerFactory.getLogger(Posts.class);
    private static final Map<String, PostRaw> slugIndex = Maps.newHashMap();
    private static final List<PostMeta> recentPosts;
    private static final Multimap<String, PostMeta> tagIndex = ArrayListMultimap.create();
    private static final Multimap<String, PostMeta> libIndex = ArrayListMultimap.create();
    static {
        List<PostRaw> posts = PostData.getPosts();

        recentPosts = Seq.seq(posts)
                         .map(Posts::metaFromPost)
                         .sorted(p -> p.getDateCreated(), Comparator.reverseOrder())
                         .toList();

        for (PostMeta post: recentPosts) {
           for (TagOrLibrary tagOrLib: post.getTagOrLibraries()) {
               Type type = tagOrLib.getType();
               if (type == Type.Library) {
                   libIndex.put(tagOrLib.getName(), post);
               } else if (type == Type.Tag) {
                   tagIndex.put(tagOrLib.getName(), post);
               } else {
                   throw new RuntimeException("Unknown type");
               }
           }
        }

        for (PostRaw post : posts) {
            slugIndex.put(post.getSlug(), post);
            logger.debug(post.getSlug());
        }
    }

    public static List<PostInfo> getRecentPostsExcluding(Set<Long> excludePostIds) {
        return CmsDSLs.transactional().transactionResult(ctx -> {
            return com.stubbornjava.cms.server.post.Posts.getRecentPostsExcluding(ctx, 1, excludePostIds);
        });
    }

    public static List<PostInfo> getRecentPosts() {
        return CmsDSLs.transactional().transactionResult(ctx -> {
            return com.stubbornjava.cms.server.post.Posts.getRecentPosts(ctx, 1);
        });
    }

    public static List<PostInfo> getRecentPosts(int num) {
        return CmsDSLs.transactional().transactionResult(ctx -> {
            return com.stubbornjava.cms.server.post.Posts.getRecentPosts(ctx, 1, num);
        });
    }

    public static List<PostMeta> getRecentPostsWithTag(String tag) {
        return Seq.seq(tagIndex.get(tag)).limit(50).toList();
    }


    public static List<PostMeta> getRecentPostsWithLibrary(String library) {
        return Seq.seq(libIndex.get(library)).limit(50).toList();
    }

    public static List<TagOrLibrary> tagCounts() {
        return findFromIndex(tagIndex, Type.Tag);
    }

    public static List<TagOrLibrary> libraryCounts() {
        return findFromIndex(libIndex, Type.Library);
    }

    public static Post findBySlug(String slug) {
        return Optional.ofNullable(slugIndex.get(slug))
                       .map(Posts::postFromMeta)
                       .orElse(null);
    }

    public static List<String> getAllSlugs() {
        return Seq.seq(slugIndex.keySet()).sorted().toList();
    }

    private static List<TagOrLibrary> findFromIndex(Multimap<String, PostMeta> index, Type type) {
        return Seq.seq(index.asMap().entrySet())
                  .map(e -> new TagOrLibrary(e.getKey(), type, e.getValue().size()))
                  .sorted(Comparator.comparing((TagOrLibrary t) -> t.getCount()).reversed())
                  .toList();
    }

    private static PostMeta metaFromPost(PostRaw postRaw) {
        List<TagOrLibrary> tagOrLibraries = Lists.newArrayList();
        tagOrLibraries.addAll(Seq.seq(postRaw.getJavaLibs())
                                 .map(l -> new TagOrLibrary(l.getName(), TagOrLibrary.Type.Library, null))
                                 .toList());
        tagOrLibraries.addAll(Seq.seq(postRaw.getTags())
                      .map(t -> new TagOrLibrary(t.getName(), TagOrLibrary.Type.Tag, null))
                      .toList());
        tagOrLibraries = Seq.seq(tagOrLibraries).sorted(e -> e.getName()).toList();
        return PostMeta.builder()
                       .postId(postRaw.getPostId())
                       .metaDesc(postRaw.getMetaDesc())
                       .tagOrLibraries(tagOrLibraries)
                       .dateCreated(postRaw.getDateCreated())
                       .title(postRaw.getTitle())
                       .slug(postRaw.getSlug())
                       .build();
    }

    private static Post postFromMeta(PostRaw postRaw) {
        PostMeta meta = metaFromPost(postRaw);
        Map<String, FileContent> fileContents = Seq.seq(postRaw.getGitFileReferences())
           .map(GitHubSource.githubClient()::getFile)
           .toMap(fc -> fc.getName());

        String content = Templating.instance().renderTemplate("templates/src/posts/" + postRaw.getSlug(), fileContents);
        String template = Resources.asString("templates/src/posts/" + postRaw.getSlug() + ".hbs");
        return Post.builder()
                   .postMeta(meta)
                   .content(content)
                   .contentTemplate(template)
                   .build();
    }

    public static void main(String[] args) {
        WebappBoostrap.run(() -> {
            List<Post> posts = Seq.seq(PostData.getPosts())
                                  .map(Posts::postFromMeta)
                                  .toList();

            for (Post post : posts) {
                PostMeta meta = post.getPostMeta();
                com.stubbornjava.cms.server.post.FullPost newPost = new com.stubbornjava.cms.server.post.FullPost(
                    null,
                    1,
                    meta.getTitle(),
                    meta.getSlug(),
                    meta.getMetaDesc(),
                    DraftStatus.PUBLISHED,
                    meta.getDateCreated(),
                    meta.getDateCreated(),
                    meta.getDateCreated().toLocalDate(),
                    post.getContentTemplate(),
                    Seq.seq(meta.getTagOrLibraries()).filter(tl -> tl.getType() == Type.Tag).map(x -> x.getName()).toSet());
                CmsDSLs.transactional().transaction(ctx -> {
                    com.stubbornjava.cms.server.post.FullPost created = com.stubbornjava.cms.server.post.Posts.create(ctx, 1, newPost);
                });
            }
        });
    }
}
