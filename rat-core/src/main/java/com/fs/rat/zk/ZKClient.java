package com.fs.rat.zk;

import com.fs.rat.exception.CannotConnectionException;
import com.fs.rat.exception.ZKClientInitException;
import com.fs.rat.exception.ZKException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Created by fanshuai on 15/6/3.
 */
public class ZKClient {
    private String zkClusterAddress;
    private CuratorListener curatorListener;
    private ConnectionStateListener connectionStateListener;
    private CuratorFramework curatorFramework ;
    public ZKClient (String zkClusterAddress){
        this.zkClusterAddress = zkClusterAddress;
    }
    public ZKClient(String zkClusterAddress,CuratorListener curatorListener,ConnectionStateListener connectionStateListener){
        this.zkClusterAddress = zkClusterAddress;
        this.connectionStateListener = connectionStateListener;
        this.curatorListener = curatorListener;
    }
    public void init() throws ZKClientInitException{
        try {
            curatorFramework = CuratorFrameworkFactory.newClient(zkClusterAddress, new ExponentialBackoffRetry(1000, 3));
            if(connectionStateListener!=null){
                curatorFramework.getConnectionStateListenable().addListener(connectionStateListener);
            }
            if(curatorListener!=null){
                curatorFramework.getCuratorListenable().addListener(curatorListener);
            }
            curatorFramework.start();
            boolean hasConnected = curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut();
            if (!hasConnected){
                throw new CannotConnectionException("zkCluster("+zkClusterAddress+") cannot connected ");
            }
        }catch (CannotConnectionException e){
            throw new ZKClientInitException(e);
        }catch (InterruptedException e){
            throw new ZKClientInitException(e);
        }
    }

    public void setData(String node,byte[] value) throws Exception {
        if(existsNode(node)){
            curatorFramework.setData().forPath(node,value);
        }
        try {
            curatorFramework.create().creatingParentsIfNeeded().forPath(node,value);
        }catch (KeeperException.NodeExistsException e){
            curatorFramework.setData().forPath(node,value);
        }
    }


    public boolean existsNode(String pathNode) throws Exception{
        Stat stat = curatorFramework.checkExists().watched().forPath(pathNode);
        if(stat==null){
            return false;
        }
        return true;
    }
    public List<String> getChildren(String nodePath,CuratorWatcher watcher) throws Exception {
        if(watcher==null){
            return  curatorFramework.getChildren().watched().forPath(nodePath);
        }else {
            return curatorFramework.getChildren().usingWatcher(watcher).forPath(nodePath);
        }
    }
    public byte[] getNodeValue(String node,CuratorWatcher watcher) throws ZKException {
        try {
            if(watcher!=null) {
                return curatorFramework.getData().usingWatcher(watcher).forPath(node);
            }else {
                return curatorFramework.getData().watched().forPath(node);
            }
        }catch (Exception e){
            throw new ZKException(e);
        }
    }
}
