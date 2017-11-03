package com.stubbornjava.webapp;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.stubbornjava.common.Configs;
import com.stubbornjava.webapp.guide.GuideTitle;
import com.stubbornjava.webapp.guide.Guides;
import com.stubbornjava.webapp.post.Posts;

import io.undertow.server.HttpServerExchange;

public class Response {
    private Map<String, Object> data;

    private Response() {
        super();
        this.data = null;
    }

    public static Response fromExchange(HttpServerExchange exchange) {
        List<GuideTitle> guideTitles = Guides.findTitles();
        return new Response()
                    .with("guideTitles", guideTitles)
                    .with("emailLists", Configs.asMap(Configs.properties().getConfig("mailchimp.lists")));
    }

    @JsonAnyGetter
    public Map<String, Object> getData() {
        return data;
    }

    public Response with(String key, Object value) {
        if (null == data) {
            data = Maps.newLinkedHashMap();
        }
        if (data.containsKey(key)) {
            throw new IllegalArgumentException(key + " already has data");
        }
        data.put(key, value);
        return this;
    }

    public Response withLibCounts() {
        return with("libraries", Posts.libraryCounts());
    }

    public Response withTagCounts() {
        return with("tags", Posts.tagCounts());
    }

    public Response withRecentPosts() {
        return with("recentPosts", Posts.getRecentPosts());
    }

    public Response withRecentPosts(int num) {
        return with("recentPosts", Posts.getRecentPosts(num));
    }

    public Response withRecentPostsExcluding(long postId) {
        return with("recentPosts", Posts.getRecentPostsExcluding(Sets.newHashSet(postId)));
    }
}
