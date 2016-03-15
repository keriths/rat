package com.fs.rat.mng.zk.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;

/**
 * Created by fanshuai on 15/6/17.
 */
public class PublishEnvironmentZKListener implements CuratorListener,ConnectionStateListener {

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {

    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {

    }
}
