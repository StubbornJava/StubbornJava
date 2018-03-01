package com.stubbornjava.common;

import java.io.File;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jooq.lambda.Seq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

// {{start:config}}
public class Configs {
    private static final Logger log = LoggerFactory.getLogger(Configs.class);

    private Configs() { }

    /*
     * I am letting the typesafe configs bleed out on purpose here.
     * We could abstract out and delegate but its not worth it.
     * I am gambling on the fact that I will not switch out the config library.
     */
    private static final Config system = ConfigFactory.systemProperties();
    private static final Config properties = new Builder().withSecureConf().envAwareApp().build();

    public static Config system() {
        return system;
    }

    public static Config properties() {
        return properties;
    }

    // This should return the current executing user path
    public static String getExecutionDirectory() {
        return system.getString("user.dir");
    }

    public static Map<String, Object> asMap(Config config) {
        return Seq.seq(config.entrySet())
                  .toMap(e -> e.getKey(), e -> e.getValue().unwrapped());
    }

    public <T> T getOrDefault(Config config, String path, Function<Config, T> extractor, T defaultValue) {
        if (config.hasPath(path)) {
            return extractor.apply(config);
        }
        return defaultValue;
    }

    public <T> T getOrDefault(Config config, String path, Function<Config, T> extractor, Supplier<T> defaultSupplier) {
        if (config.hasPath(path)) {
            return extractor.apply(config);
        }
        return defaultSupplier.get();
    }

    public static class Builder {
        private Config conf;

        public Builder() {
            log.info("Loading configs first row is highest priority, second row is fallback and so on");
        }

        public Builder withResource(String resource) {
            conf = returnOrFallback(ConfigFactory.parseResources(resource));
            log.info("Loaded config file from resource ({})", resource);
            return this;
        }

        public Builder withOptionalFile(String path) {
            File secureConfFile = new File(path);
            if (secureConfFile.exists()) {
                log.info("Loaded config file from path ({})", path);
                conf = returnOrFallback(ConfigFactory.parseFile(secureConfFile));
            } else {
                log.info("Attempted to load file from path ({}) but it was not found", path);
            }
            return this;
        }

        public Builder envAwareApp() {
            String env = system.hasPath("env") ? system.getString("env") : "local";
            String envFile = "application." + env + ".conf";
            return withResource(envFile).withResource("application.conf");
        }

        public Builder withSecureConf() {
            return withOptionalFile(getExecutionDirectory() + "/secure.conf");
        }

        public Config build() {
            // Resolve substitutions.
            conf = conf.resolve();
            if (log.isDebugEnabled()) {
                log.debug("Logging properties. Make sure sensitive data such as passwords or secrets are not logged!");
                log.debug(conf.root().render(ConfigRenderOptions.concise().setFormatted(true)));
            }
            return conf;
        }

        private Config returnOrFallback(Config config) {
            if (this.conf == null) {
                return config;
            }
            return this.conf.withFallback(config);
        }
    }

    public static void main(String[] args) {
        Configs.properties();
    }
}
// {{end:config}}