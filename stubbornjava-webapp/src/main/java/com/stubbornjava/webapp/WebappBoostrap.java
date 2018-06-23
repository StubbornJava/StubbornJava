package com.stubbornjava.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stubbornjava.common.Configs;
import com.stubbornjava.common.Env;
import com.stubbornjava.common.Json;
import com.typesafe.config.Config;

public class WebappBoostrap {
    private static final Logger logger = LoggerFactory.getLogger(WebappBoostrap.class);

    public static Config getConfig() {
        Config config = Configs.newBuilder()
           .withOptionalRelativeFile("./secure.conf")
           .withResource("sjweb." + Env.get().getName() + ".conf")
           .withResource("sjweb.conf")
           .withResource("cms.application." + Env.get().getName() + ".conf")
           .withResource("cms.application.conf")
           .withResource("application." + Env.get().getName() + ".conf")
           .withResource("application.conf")
           .build();
        logger.debug(Json.serializer().toPrettyString(Configs.asMap(config)));
        return config;
    }

    public static <T> void run(Runnable runnable) {
        try {
            Configs.initProperties(getConfig());
            runnable.run();
        } catch (Throwable ex) {
            logger.error("", ex);
        } finally {
            // Close pools and stuff
        }
    }
}
