package com.stubbornjava.common;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import com.typesafe.config.Config;

public class ConfigsTest {

    public void emptyConfigShouldNotFail() {
        new Configs.Builder().build();
    }

    public void configShouldLoadResource() {
        Config conf = new Configs.Builder()
                                 .withResource("other.conf")
                                 .build();
        assertEquals("other", conf.getString("name"));
    }

    public void configShouldLoadAppConfig() {
        Config conf = new Configs.Builder()
                                 .envAwareApp()
                                 .build();
        assertEquals("StubbornJava Common", conf.getString("app"));
        // 2 minutes is the override local config.
        assertEquals(2, conf.getDuration("someTimeout", TimeUnit.MINUTES));
    }
}
