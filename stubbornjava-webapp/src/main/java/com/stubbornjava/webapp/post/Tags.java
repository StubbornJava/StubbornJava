package com.stubbornjava.webapp.post;

import java.util.List;

import com.google.common.collect.Lists;

public class Tags {
    private static final List<Tag> TAGS = Lists.newArrayList();
    public static final Tag JSON = addTag(new Tag(811790051562631559L, "JSON"));
    public static final Tag Configuration = addTag(new Tag(811790051562631558L, "Configuration"));
    public static final Tag HtmlTemplating = addTag(new Tag(816019962631460110L, "HTML Templating"));
    public static final Tag WebServer = addTag(new Tag(818151980207497304L, "Web Server"));
    public static final Tag ExceptionHandling = addTag(new Tag(818952366670354734L, "Exception Handling"));
    public static final Tag Middleware = addTag(new Tag(818954428212386484L, "Middleware"));
    public static final Tag Logging = addTag(new Tag(819164010986060924L, "Logging"));
    public static final Tag REST = addTag(new Tag(821046967098928983L, "REST"));
    public static final Tag Monitoring = addTag(new Tag(821921404859338502L, "Monitoring"));
    public static final Tag HTTP = addTag(new Tag(828947285321090457L, "HTTP"));
    public static final Tag SQL = addTag(new Tag(828947285308560598L, "SQL"));
    public static final Tag Gradle = addTag(new Tag(833715638133220197L, "Gradle"));
    public static final Tag Enums = addTag(new Tag(834409923387696842L, "Enums"));
    public static final Tag Webpack = addTag(new Tag(876957148741446344L, "Webpack"));
    public static final Tag NPM = addTag(new Tag(876957148739332593L, "NPM"));
    public static final Tag SEO = addTag(new Tag(880770018813220697L, "SEO"));
    public static final Tag Microservice = addTag(new Tag(897667693917183622L, "Microservice"));
    public static final Tag Monolith = addTag(new Tag(897667693920667427L, "Monolith"));
    public static final Tag Caching = addTag(new Tag(905411988101360294L, "Caching"));
    public static final Tag Docker = addTag(new Tag(922789907642700121L, "Docker"));
    public static final Tag MySQL = addTag(new Tag(922794262771082027L, "MySQL"));
    public static final Tag Elasticsearch = addTag(new Tag(922794262770139008L, "Elasticsearch"));
    public static final Tag Ansible = addTag(new Tag(922794262770139008L, "Ansible"));
    public static final Tag Supervisord = addTag(new Tag(922794262770139008L, "Supervisord"));
    public static final Tag Security = addTag(new Tag(953801444178362856L, "Security"));
    public static final Tag Resiliency = addTag(new Tag(958330984838442254L, "Resiliency"));

    private static Tag addTag(Tag tag) {
        TAGS.add(tag);
        return tag;
    }

    public static List<Tag> getTags() {
        return TAGS;
    }
}
