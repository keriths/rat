package com.fs.rat.mng.zk;

import com.fs.rat.mng.zk.ZKClient;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.state.ConnectionStateListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fanshuai on 15/6/17.
 */
public class ZKClientFactory {
    private static Map<String,ZKClient> cleints = new ConcurrentHashMap<String,ZKClient>();
    public static ZKClient getZKClient(String zkClusterAddress){
        if(cleints.get(zkClusterAddress)==null){
            synchronized (zkClusterAddress){
                if(cleints.get(zkClusterAddress)==null){
                    cleints.put(zkClusterAddress,new ZKClient(zkClusterAddress));
                }
            }
        }
        return cleints.get(zkClusterAddress);
    }
    public static ZKClient getZKClient(String zkClusterAddress,ConnectionStateListener connectionStateListener,CuratorListener curatorListener){
        ZKClient zkClient = getZKClient(zkClusterAddress);
        if(zkClient==null){
            return null;
        }
        zkClient.addConnectionStateListenable(connectionStateListener);
        zkClient.addCuratorListenable(curatorListener);
        return zkClient;
    }
}
