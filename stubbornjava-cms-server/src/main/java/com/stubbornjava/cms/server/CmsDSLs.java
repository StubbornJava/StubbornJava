package com.stubbornjava.cms.server;

import com.stubbornjava.common.db.ConfigurationWrapper;

public class CmsDSLs {

    private CmsDSLs() {}

    public static ConfigurationWrapper transactional() {
        return new ConfigurationWrapper(CMSConnectionPools.transactionalConfig());
    }

    public static ConfigurationWrapper processing() {
        return new ConfigurationWrapper(CMSConnectionPools.processingConfig());
    }
}
