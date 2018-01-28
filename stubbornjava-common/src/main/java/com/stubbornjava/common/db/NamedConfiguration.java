package com.stubbornjava.common.db;

import org.jooq.Configuration;

public class NamedConfiguration {
    private final String name;
    private final Configuration configuration;

    public NamedConfiguration(String name, Configuration configuration) {
        super();
        this.name = name;
        this.configuration = configuration;
    }

    public String getName() {
        return name;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
