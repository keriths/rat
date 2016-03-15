package com.fs.rat.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.Watcher;

/**
 * Created by fanshuai on 15/6/3.
 */
public class RatClientCuratorListener implements CuratorListener {
    public RatZKClient ratZKClient;
    public RatClientCuratorListener(RatZKClient ratZKClient){
        this.ratZKClient = ratZKClient;
    }
    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
        if(event.getWatchedEvent().getType().getIntValue()== Watcher.Event.EventType.NodeChildrenChanged.getIntValue()){
            String parentNode = event.getPath();
            System.out.println(parentNode);
//            String domain = "";
//            ratZKClient.flushDomainCachedPropertyValue(domain);
            if(RatZKClient.propertyBaseZKNode.equals(parentNode)){
                //rat 根目录变更添加了domain

            }else {
                if(RatZKClient.notExistsNode.containsKey(parentNode)){
                    //domain下面添加了新的

                }
            }
            ratZKClient.flushCachedPropertyValue();
        }
        if(event.getWatchedEvent().getType().getIntValue()== Watcher.Event.EventType.NodeDataChanged.getIntValue()){
            String propertyNode = event.getPath();
            ratZKClient.flushCachedPropertyValue();
        }
    }
}
