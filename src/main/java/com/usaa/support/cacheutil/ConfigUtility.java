package com.usaa.support.cacheutil;

import com.hazelcast.client.config.ClientClasspathXmlConfig;
import com.hazelcast.client.config.ClientConfig;

public class ConfigUtility {

    public static ClientConfig createClientConfig(Arguments arguments)
    {
        ClientConfig clientConfig = new ClientClasspathXmlConfig(arguments.getXmlFile());
        return clientConfig;
    }

}
