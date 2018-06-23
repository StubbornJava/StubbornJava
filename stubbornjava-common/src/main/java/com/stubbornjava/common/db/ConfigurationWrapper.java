package com.stubbornjava.common.db;

import java.util.function.Consumer;
import java.util.function.Function;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public class ConfigurationWrapper {

    private final Configuration configuration;

    public ConfigurationWrapper(Configuration configuration) {
        this.configuration = configuration;
    }

    public void transaction(Consumer<DSLContext> consumer) {
        DSL.using(configuration).transaction(ctx -> consumer.accept(DSL.using(ctx)));
    }

    public <T> T transactionResult(Function<DSLContext, T> consumer) {
        return DSL.using(configuration).transactionResult(ctx -> consumer.apply(DSL.using(ctx)));
    }
}
