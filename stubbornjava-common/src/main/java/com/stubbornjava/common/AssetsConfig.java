package com.stubbornjava.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class AssetsConfig {
    private static final Logger log = LoggerFactory.getLogger(AssetsConfig.class);

    public static String assetsRoot() {
        if (!Configs.properties().hasPath("assets.root")) {
            log.error("Configuration is missing required \"assets.root\" config for dev mode. Did you forget to add it to application.local.conf?");
            Preconditions.checkNotNull(null, "Missing required \"assets.root\" config.");
        }
        return Configs.properties().getString("assets.root");
    }
}
