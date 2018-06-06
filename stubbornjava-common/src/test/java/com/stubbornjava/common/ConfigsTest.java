package com.stubbornjava.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.typesafe.config.Config;

public class ConfigsTest {

    @Test
    public void emptyConfigShouldNotFail() {
        new Configs.Builder().build();
    }

    @Test
    public void configShouldLoadResource() {
        Config conf = new Configs.Builder()
                                 .withResource("other.conf")
                                 .build();
        assertEquals("other", conf.getString("name"));
    }
}
