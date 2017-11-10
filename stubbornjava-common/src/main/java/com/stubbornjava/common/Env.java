package com.stubbornjava.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Env {
    LOCAL("local")
    , DEV("dev")
    , PROD("prod")
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
        if (Configs.system().hasPath("env")) {
            env = Configs.system().getString("env");
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
