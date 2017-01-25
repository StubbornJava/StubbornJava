package com.stubbornjava.common;

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

    public static Env get() {
        String env = "local";
        if (Configs.properties().hasPath("env")) {
            env = Configs.properties().getString("env");
        }
        return Env.valueOf(env.toUpperCase());
    }
}
