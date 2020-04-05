package com.stubbornjava.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Env {
    LOCAL("local")
    , DEV("development")
    , STAGING("staging")
    , PROD("production")
    ;

    private final String name;
    Env(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // {{start:logger}}
    private static final Logger log = LoggerFactory.getLogger(Env.class);
    private static final Env currentEnv;
    static {
        String env = "local";
        // This comes from -Denv={environment}
        if (Configs.systemProperties().hasPath("env")) {
            env = Configs.systemProperties().getString("env");
            log.info("Found env setting {} in system properties", env);
        } else if (Configs.systemEnvironment().hasPath("ENV")) {
            env = Configs.systemEnvironment().getString("ENV");
            log.info("Found env setting {} in env variables", env);
        } else if (Configs.systemEnvironment().hasPath("env")) {
            env = Configs.systemEnvironment().getString("env");
            log.info("Found ENV setting {} in env variables", env);
        }
        currentEnv = Env.valueOf(env.toUpperCase());
        log.info("Current Env: {}", currentEnv.getName());
    }

    public static Env get() {
        return currentEnv;
    }

    public static void main(String[] args) {
        Env env = Env.get();
        log.debug(env.toString());
    }
    // {{end:logger}}
}
