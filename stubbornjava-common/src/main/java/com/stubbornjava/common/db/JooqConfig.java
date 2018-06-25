package com.stubbornjava.common.db;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.meta.jaxb.ForcedType;

import com.google.common.collect.Lists;

public class JooqConfig {

    public static Configuration defaultConfigFromDataSource(DataSource ds) {
        DataSourceConnectionProvider dcp = new DataSourceConnectionProvider(ds);
        Configuration jooqConfig = new DefaultConfiguration();
        jooqConfig.set(SQLDialect.MYSQL);
        jooqConfig.set(dcp);
        //jooqConfig.set(new ThreadLocalTransactionProvider(dcp));
        jooqConfig.settings()
                  .withExecuteWithOptimisticLockingExcludeUnversioned(true);
        return jooqConfig;
    }

    public static List<ForcedType> defaultForcedTypes() {
        return Lists.newLinkedList(Arrays.asList(new ForcedType[] {
                new ForcedType()
                    .withName("BOOLEAN")
                    .withTypes("tinyint")
        }));
    }
}
