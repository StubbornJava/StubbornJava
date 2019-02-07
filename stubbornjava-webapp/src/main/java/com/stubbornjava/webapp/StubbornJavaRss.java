package com.stubbornjava.webapp;

import java.io.StringWriter;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.jooq.lambda.Seq;
import org.jooq.lambda.Unchecked;

import com.stubbornjava.common.undertow.Exchange;
import com.stubbornjava.webapp.post.PostRaw;
import com.stubbornjava.webapp.post.Posts;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

import io.undertow.server.HttpServerExchange;
import okhttp3.HttpUrl;

class StubbornJavaRss {


    public static void getRssFeed(HttpServerExchange exchange) {
        HttpUrl host = Exchange.urls().host(exchange);
        Exchange.body().sendXml(exchange, getFeed(host));
    }

    private static String getFeed(HttpUrl host) {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("StubbornJava");
        feed.setLink(host.toString());
        feed.setDescription("Unconventional guides, examples, and blog utilizing modern Java");

        List<PostRaw> posts = Posts.getAllRawPosts();
        List<SyndEntry> entries = Seq.seq(posts)
            .map(p -> {
                SyndEntry entry = new SyndEntryImpl();
                entry.setTitle(p.getTitle());
                entry.setLink(host.newBuilder().encodedPath(p.getUrl()).build().toString());
                entry.setPublishedDate(Date.from(p.getDateCreated()
                                                  .toLocalDate()
                                                  .atStartOfDay(ZoneId.systemDefault())
                                                  .toInstant()));
                entry.setUpdatedDate(Date.from(p.getDateUpdated()
                                                .toLocalDate()
                                                .atStartOfDay(ZoneId.systemDefault())
                                                .toInstant()));
                SyndContentImpl description = new SyndContentImpl();
                description.setType("text/plain");
                description.setValue(p.getMetaDesc());
                entry.setDescription(description);
                return entry;
            }).toList();
        feed.setEntries(entries);

        StringWriter writer = new StringWriter();
        SyndFeedOutput output = new SyndFeedOutput();

        return Unchecked.supplier(() -> {
            output.output(feed, writer);
            return writer.toString();
        }).get();
    }
}
