package com.stubbornjava.webapp.guide;

import java.util.List;
import java.util.Map;

import org.jooq.lambda.Seq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.stubbornjava.common.Json;
import com.stubbornjava.common.Slugs;
import com.stubbornjava.common.Templating;

public class Guides {
    private static final Logger log = LoggerFactory.getLogger(Guides.class);

    private Guides() {}
    private static final Map<String, Guide> guideSlugIndex;
    private static final List<GuideTitle> titles;
    static {
        List<Guide> guides = Seq.of(
            buildGuide("Embedded Java Web Server")
        ).toList();

        guideSlugIndex = Seq.seq(guides).toMap(Guide::getSlug);
        titles = ImmutableList.copyOf(Seq.seq(guides).map(g -> new GuideTitle(g.getTitle(), g.getSlug())).toList());
    }

    private static Guide buildGuide(String title) {
        String slug = Slugs.toSlug(title);
        String content = Templating.instance().renderTemplate("templates/src/guides/" + slug);
        Guide guide = new Guide(title, slug, content);
        return guide;
    }

    public static List<GuideTitle> findTitles() {
        return titles;
    }

    public static Guide findBySlug(String slug) {
        if (null == slug) {
            return null;
        }
        return guideSlugIndex.get(slug);
    }

    public static void main(String[] args) {
        log.debug(Json.serializer().toPrettyString(findBySlug("embedded-java-web-server")));
    }
}
