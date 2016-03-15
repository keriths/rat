package com.fs.rat.mng.zk;

import com.fs.rat.mng.zk.exception.ZKClientInitException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fanshuai on 15/6/17.
 */
public class ZKClient {
    private List<ConnectionStateListener> addedConnectionStateListeners = new ArrayList<ConnectionStateListener>();
    private List<CuratorListener> addedCuratorListeners = new ArrayList<CuratorListener>();
    private CuratorFramework curatorFramework ;
    private String zkClusterAddress;
    private int baseSleepTimeMs = 1000;
    private int maxRetries = 3;
    private boolean isConnected = false;
    public ZKClient(String zkClusterAddress){
        this.zkClusterAddress = zkClusterAddress;
        connection();
    }
    public ZKClient(String zkClusterAddress,CuratorListener curatorListener,ConnectionStateListener connectionStateListener){
        this.zkClusterAddress = zkClusterAddress;
        connection();
        addConnectionStateListenable(connectionStateListener);
        addCuratorListenable(curatorListener);
    }
    public ZKClient(String zkClusterAddress,ConnectionStateListener connectionStateListener){
        this.zkClusterAddress = zkClusterAddress;
        connection();
        addConnectionStateListenable(connectionStateListener);
    }
    public ZKClient(String zkClusterAddress,CuratorListener curatorListener){
        this.zkClusterAddress = zkClusterAddress;
        connection();
        addCuratorListenable(curatorListener);
    }
    public ZKClient(String zkClusterAddress,int baseSleepTimeMs, int maxRetries ,CuratorListener curatorListener,ConnectionStateListener connectionStateListener){
        this.zkClusterAddress = zkClusterAddress;
        if(baseSleepTimeMs>0) {
            this.baseSleepTimeMs = baseSleepTimeMs;
        }
        if(maxRetries>0){
            this.maxRetries = maxRetries;
        }
        connection();
        addConnectionStateListenable(connectionStateListener);
        addCuratorListenable(curatorListener);
    }
    private void createCuratorFramework(){
        if(curatorFramework==null){
            synchronized(this){
                if(curatorFramework==null) {
                    curatorFramework = CuratorFrameworkFactory.newClient(zkClusterAddress, new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries));
                }
            }
        }
    }
    private void connection(){
        try {
            createCuratorFramework();
            curatorFramework.start();
            if(!curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()){
                throw new Exception(" connot connection zkServer("+zkClusterAddress+") ");
            }
        }catch (Exception e){
            throw new ZKClientInitException(e);
        }
    }
    public void reConnection(){
        try {
            curatorFramework.start();
            if(!curatorFramework.getZookeeperClient().blockUntilConnectedOrTimedOut()){
                throw new Exception(" connot connection zkServer("+zkClusterAddress+") ");
            }
        }catch (Exception e){
            throw new ZKClientInitException(e);
        }
    }

    public void addConnectionStateListenable(ConnectionStateListener connectionStateListener){
        if(connectionStateListener==null){
            return ;
        }
        if(addedConnectionStateListeners.contains(connectionStateListener)){
            return ;
        }
        this.curatorFramework.getConnectionStateListenable().addListener(connectionStateListener);
    }

    public void addCuratorListenable(CuratorListener curatorListener){
        if(curatorListener==null){
            return ;
        }
        if(addedCuratorListeners.contains(curatorListener)){
            return;
        }
        this.curatorFramework.getCuratorListenable().addListener(curatorListener);
    }

    public byte[] getData(String node,boolean isWatcher)throws Exception{
        if(isWatcher){
            return this.curatorFramework.getData().watched().forPath(node);
        }else {
            return this.curatorFramework.getData().forPath(node);
        }
    }
    public byte[] getData(String node,CuratorWatcher watcher)throws Exception{
        return this.curatorFramework.getData().usingWatcher(watcher).forPath(node);
    }
    public String getDataToString(String node,boolean isWatcher,String encoding)throws Exception{
        byte[] byteDatas = getData(node,isWatcher);
        if(byteDatas==null || byteDatas.length==0){
            return null;
        }
        if(encoding!=null){
            return new String(byteDatas,encoding);
        }else {
            return new String(byteDatas);
        }
    }
    public String getDataToString(String node,CuratorWatcher watcher,String encoding)throws Exception{
        byte[] byteDatas = getData(node,watcher);
        if(byteDatas==null || byteDatas.length==0){
            return null;
        }
        if(encoding!=null){
            return new String(byteDatas,encoding);
        }else {
            return new String(byteDatas);
        }
    }
    public String getDataToString(String node,boolean isWatcher)throws Exception{
        return getDataToString(node,isWatcher,null);
    }
    public String getDataToString(String node,CuratorWatcher watcher)throws Exception{
        return getDataToString(node,watcher,null);
    }
    public List<String> getChildren(String node,boolean isWatcher)throws Exception{
        if(isWatcher){
            return this.curatorFramework.getChildren().watched().forPath(node);
        }else{
            return this.curatorFramework.getChildren().forPath(node);
        }
    }
    public List<String> getChildren(String node,CuratorWatcher watcher)throws Exception{
        return this.curatorFramework.getChildren().usingWatcher(watcher).forPath(node);
    }

    public void create(String node,byte [] data)throws Exception{
        this.curatorFramework.create().forPath(node, data);
    }
    public void setData(String node,byte [] data)throws Exception{
        this.curatorFramework.setData().forPath(node,data);
    }


    public boolean isConnected(){
        return  this.curatorFramework.getZookeeperClient().isConnected();
    }
    public boolean exists(String node)throws Exception{
        Stat stat = curatorFramework.checkExists().forPath(node);
        if(stat==null){
            return false;
        }
        return true;
    }
}
