package com.fs.rat.client;

import com.fs.rat.exception.CannotConnectionException;
import com.fs.rat.exception.ZKClientInitException;
import com.fs.rat.zk.RatZKClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fanshuai on 15/6/2.
 */
public class RatPropertyClientFactory {
    private static RatPropertyClient defaultClient;
    private static Map<String,RatPropertyClient> zkRatClientMap = new HashMap<String, RatPropertyClient>();
    public static RatPropertyClient getDefaultRatClient() throws ZKClientInitException {
        if (defaultClient == null){
            synchronized (RatPropertyClientFactory.class){
                if(defaultClient == null){
                    defaultClient = new RatZKClient();
                }
            }
        }
        return defaultClient;
    }
    public static RatPropertyClient getZKRatClient(String zkClusterHost) throws ZKClientInitException {
        RatPropertyClient ratPropertyClient = zkRatClientMap.get(zkClusterHost);
        if(ratPropertyClient==null){
            synchronized (zkClusterHost){
                ratPropertyClient = zkRatClientMap.get(zkClusterHost);
                if(ratPropertyClient==null){
                    ratPropertyClient = new RatZKClient(zkClusterHost);
                    zkRatClientMap.put(zkClusterHost,ratPropertyClient);
                }
            }
        }
        return ratPropertyClient;
    }
}
