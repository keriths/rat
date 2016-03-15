package com.fs.rat.client;

/**
 * Created by fanshuai on 15/6/2.
 */
public interface RatPropertyClient {
    /**
     * get the key value if none return defaultVal
     * @param key
     * @param defaultVal
     * @return
     */
    String getPropertyValue(String key,String defaultVal);

    /**
     * get the key value if none return null
     * @param key
     * @return
     */
    String getPropertyValue(String key);
}
