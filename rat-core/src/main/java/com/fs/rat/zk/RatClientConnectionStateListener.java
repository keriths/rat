package com.fs.rat.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * Created by fanshuai on 15/6/3.
 */
public class RatClientConnectionStateListener implements ConnectionStateListener {
    public RatZKClient ratZKClient;
    public RatClientConnectionStateListener(RatZKClient ratZKClient){
        this.ratZKClient = ratZKClient;
    }
    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        if (newState == ConnectionState.LOST) {
            while (true) {
                try {
                    if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                        break;
                    }
                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                }
            }
        }else if(newState == ConnectionState.RECONNECTED){
            ratZKClient.flushCachedPropertyValue();
        }
    }
}
