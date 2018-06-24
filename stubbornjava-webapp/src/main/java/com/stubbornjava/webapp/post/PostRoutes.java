package com.stubbornjava.webapp.post;

import java.util.List;

import com.stubbornjava.cms.server.post.PostMeta;
import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.webapp.PageRoutes;
import com.stubbornjava.webapp.Response;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

public class PostRoutes {

    public static void getPost(HttpServerExchange exchange) {
        String slug = Exchange.pathParams().pathParam(exchange, "slug").orElse("");
        Post post = Posts.findBySlug(slug);

        if (null == post) {
            PageRoutes.notFound(exchange);
            return;
        }

        Response response = Response.fromExchange(exchange)
                .with("post", post)
                .withLibCounts()
                .withRecentPostsExcluding(post.getPostMeta().getPostId());
        Exchange.body().sendHtmlTemplate(exchange, "templates/src/pages/post", response);
    }

    public static void recentPostsWithTag(HttpServerExchange exchange) {
        String tag = Exchange.queryParams().queryParam(exchange, "tag").orElse("");
        List<PostMeta> posts = Posts.getRecentPostsWithTag(tag);

        boolean noData = posts.size() < 1;
        if (noData) {
            exchange.setStatusCode(StatusCodes.NOT_FOUND);
        }

        Response response = Response.fromExchange(exchange)
                .with("posts", posts)
                .with("type", "Tag")
                .with("value", tag)
                .with("noData", noData)
                .withLibCounts()
                .withRecentPosts();
        Exchange.body().sendHtmlTemplate(exchange, "templates/src/pages/tagOrLibSearch", response);
    }
}
