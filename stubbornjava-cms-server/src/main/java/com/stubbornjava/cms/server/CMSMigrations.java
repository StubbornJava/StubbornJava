package com.stubbornjava.cms.server;

import java.util.List;

import org.flywaydb.core.Flyway;
import org.jooq.codegen.GenerationTool;
import org.jooq.lambda.Unchecked;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.ForcedType;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Strategy;
import org.jooq.meta.jaxb.Target;
import org.jooq.meta.mysql.MySQLDatabase;

import com.mysql.jdbc.Driver;
import com.stubbornjava.common.db.CustomGeneratorStrategy;
import com.stubbornjava.common.db.JooqConfig;
import com.zaxxer.hikari.HikariDataSource;


public class CMSMigrations {

    public static void migrate() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(CMSConnectionPools.processing());
        flyway.setBaselineOnMigrate(true);
        flyway.setLocations("db/cms/migration");
        flyway.setSqlMigrationPrefix("V_");
        flyway.setTable("_flyway");
        flyway.migrate();
    }

    public static void codegen() throws Exception {
        List<ForcedType> forcedTypes = JooqConfig.defaultForcedTypes();

        HikariDataSource ds = CMSConnectionPools.processing();

        Configuration configuration = new Configuration()
            .withJdbc(new Jdbc()
                .withDriver(Driver.class.getName())
                .withUrl(ds.getJdbcUrl())
                .withUser(ds.getUsername())
                .withPassword(ds.getPassword()))
            .withGenerator(new Generator()
                .withDatabase(new Database()
                    .withName(MySQLDatabase.class.getName())
                    .withIncludes(".*")
                    .withExcludes("")
                    .withIncludeExcludeColumns(true)
                    .withForcedTypes(forcedTypes)
                    .withInputSchema("sj_cms"))
                .withGenerate(new Generate()
                    .withJavaTimeTypes(true))
                .withStrategy(new Strategy()
                    .withName(CustomGeneratorStrategy.class.getName()))
                .withTarget(new Target()
                    .withPackageName("com.stubbornjava.cms.server.generated")
                    .withDirectory("src/generated/java")));

        GenerationTool.generate(configuration);
    }

    public static void main(String[] args) throws Exception {
        CMSBootstrap.run(Unchecked.runnable(() -> {
            migrate();
            codegen();
        }));
    }
}
