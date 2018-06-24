package com.stubbornjava.cms.server;

import java.util.List;

import org.flywaydb.core.Flyway;
import org.jooq.lambda.Unchecked;
import org.jooq.util.GenerationTool;
import org.jooq.util.jaxb.Configuration;
import org.jooq.util.jaxb.Database;
import org.jooq.util.jaxb.ForcedType;
import org.jooq.util.jaxb.Generate;
import org.jooq.util.jaxb.Generator;
import org.jooq.util.jaxb.Jdbc;
import org.jooq.util.jaxb.Strategy;
import org.jooq.util.jaxb.Target;
import org.jooq.util.mysql.MySQLDatabase;

import com.mysql.jdbc.Driver;
import com.stubbornjava.cms.server.post.DraftStatus;
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

        forcedTypes.add(new ForcedType()
            .withUserType(DraftStatus.class.getName())
            .withEnumConverter(true)
            .withExpression(".*draft_status.*"));

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
