package com.stubbornjava.webapp;

import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.FetchQuery;
import com.stubbornjava.common.Configs;
import com.typesafe.config.Config;

public class ContentfulClient {
    private static final Config conf = Configs.properties().getConfig("contentful.stubbornjava");
    private static final CDAClient client = CDAClient.builder()
        .setSpace(conf.getString("space"))
        .setToken(conf.getString("token"))
        .build();

    public static CDAClient getClient() {
        return client;
    }

    public static FetchQuery<CDAEntry> dsl() {
        return client.fetch(CDAEntry.class);
    }
}
