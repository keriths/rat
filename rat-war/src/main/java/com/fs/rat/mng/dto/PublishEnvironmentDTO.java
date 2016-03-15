package com.fs.rat.mng.dto;

import java.io.Serializable;

/**
 * 发布环境如生产，测试，开发各自对应自己的集群地址
 * Created by fanshuai on 15/6/17.
 */
public class PublishEnvironmentDTO implements Serializable{

    private String name;
    private String zkClusterAddress;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZkClusterAddress() {
        return zkClusterAddress;
    }

    public void setZkClusterAddress(String zkClusterAddress) {
        this.zkClusterAddress = zkClusterAddress;
    }
}
