package com.stubbornjava.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

// {{start:config}}
public class Configs {
    private static final Logger logger = LoggerFactory.getLogger(Configs.class);
    /*
     * I am letting the typesafe configs bleed out on purpose here.
     * We could abstract out and delegate but its not worth it.
     * I am gambling on the fact that I will not switch out the config library.
     */
    private static final Config system = ConfigFactory.systemProperties();
    private static final Config properties = new Builder().envAwareApp().build();

    public static Config system() {
        return system;
    }

    public static Config properties() {
        return properties;
    }

    public static class Builder {
        private final List<String> configs;

        public Builder() {
            this.configs = Lists.newLinkedList();
        }

        public Builder withResource(String resource) {
            configs.add(resource);
            return this;
        }

        public Builder envAwareApp() {
            String env = system.hasPath("env") ? system.getString("env") : "local";
            String envFile = "application." + env + ".conf";
            configs.add(envFile);
            configs.add("application.conf");
            return this;
        }

        public Config build() {
            logger.info("Loading configs first row is highest priority, second row is fallback and so on");
            configs.forEach(logger::info);
            Preconditions.checkArgument(configs.size() > 0, "No config resources specified!");
            Config appConfig = ConfigFactory.parseResources(configs.remove(0));
            for (String resource : configs) {
                appConfig = appConfig.withFallback(ConfigFactory.parseResources(resource));
            }

            // Resolve substitutions.
            appConfig = appConfig.resolve();

            logger.debug("Logging properties. Make sure sensitive data such as passwords or secrets are not logged!");
            logger.debug(appConfig.root().render(ConfigRenderOptions.concise().setFormatted(true)));
            return appConfig;
        }
    }

    public static void main(String[] args) {
        Configs.properties();
    }
}
// {{end:config}}