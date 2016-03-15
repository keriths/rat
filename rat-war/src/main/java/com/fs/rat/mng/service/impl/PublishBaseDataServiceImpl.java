package com.fs.rat.mng.service.impl;

import com.alibaba.fastjson.JSON;
import com.fs.rat.mng.dto.PublishEnvironmentDTO;
import com.fs.rat.mng.dto.PublishProjectDTO;
import com.fs.rat.mng.service.PublishBaseDataService;
import com.fs.rat.mng.zk.ZKClient;
import com.fs.rat.mng.zk.ZKClientFactory;
import com.fs.rat.mng.zk.listener.PublishEnvironmentZKListener;
import com.google.common.base.Strings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanshuai on 15/6/17.
 */
@Service("publishBaseDataService")
public class PublishBaseDataServiceImpl implements PublishBaseDataService,InitializingBean {
    private String publishEnvBasePath="/rat/publish/environment/";
    private String publishProjectBasePath="/rat/publish/project/";
    /**
     * 基础数据包括，发布环境》集群地址，项目目录
     */
    @Value("rat.env.zkAddress")
    private String ratBaseDataZKClusterAddress;

    @Override
    public List<PublishEnvironmentDTO> getPublishEnvs() throws Exception{
        List<PublishEnvironmentDTO> list = new ArrayList<PublishEnvironmentDTO>();
        ZKClient zkClient = ZKClientFactory.getZKClient(ratBaseDataZKClusterAddress);
        List<String> environments = zkClient.getChildren(publishEnvBasePath, true);
        for (String envNode:environments){
            String envData = zkClient.getDataToString(publishEnvBasePath + envNode, true);
            PublishEnvironmentDTO publishEnvironmentDTO = JSON.toJavaObject(JSON.parseObject(envData),PublishEnvironmentDTO.class);
            list.add(publishEnvironmentDTO);
        }
        return list;
    }

    @Override
    public void addPublishEnv(PublishEnvironmentDTO publishEnvironmentDTO)throws Exception{
        ZKClient zkClient = ZKClientFactory.getZKClient(ratBaseDataZKClusterAddress);
        if(zkClient.exists(publishEnvBasePath+publishEnvironmentDTO.getName())){
            throw new Exception(publishEnvironmentDTO.getName()+" env has been exist ");
        }
        String data = JSON.toJSONString(publishEnvironmentDTO);
        zkClient.create(publishEnvBasePath+publishEnvironmentDTO.getName(),data.getBytes("utf-8"));
    }

    @Override
    public void editPublishEnv(PublishEnvironmentDTO publishEnvironmentDTO)throws Exception{
        ZKClient zkClient = ZKClientFactory.getZKClient(ratBaseDataZKClusterAddress);
        if(!zkClient.exists(publishEnvBasePath+publishEnvironmentDTO.getName())){
            throw new Exception(publishEnvironmentDTO.getName()+" env has not exist ");
        }
        String data = JSON.toJSONString(publishEnvironmentDTO);
        zkClient.setData(publishEnvBasePath + publishEnvironmentDTO.getName(), data.getBytes("utf-8"));
    }

    @Override
    public List<PublishProjectDTO> getPublishProjects() throws Exception{
        List<PublishProjectDTO> list = new ArrayList<PublishProjectDTO>();
        ZKClient zkClient = ZKClientFactory.getZKClient(ratBaseDataZKClusterAddress);
        List<String> environments = zkClient.getChildren(publishProjectBasePath, true);
        for (String envNode:environments){
            String envData = zkClient.getDataToString(publishProjectBasePath + envNode, true);
            PublishProjectDTO publishProjectDTO = JSON.toJavaObject(JSON.parseObject(envData),PublishProjectDTO.class);
            list.add(publishProjectDTO);
        }
        return list;
    }

    @Override
    public void addPublishProject(PublishProjectDTO publishProjectDTO)throws Exception{
        ZKClient zkClient = ZKClientFactory.getZKClient(ratBaseDataZKClusterAddress);
        if(zkClient.exists(publishProjectBasePath+publishProjectDTO.getProjectName())){
            throw new Exception(publishProjectDTO.getProjectName()+" env has been exist ");
        }
        String data = JSON.toJSONString(publishProjectDTO);
        zkClient.create(publishProjectBasePath+publishProjectDTO.getProjectName(),data.getBytes("utf-8"));
    }

    @Override
    public void editPublishProject(PublishProjectDTO publishProjectDTO)throws Exception{
        ZKClient zkClient = ZKClientFactory.getZKClient(ratBaseDataZKClusterAddress);
        if(!zkClient.exists(publishProjectBasePath+publishProjectDTO.getProjectName())){
            throw new Exception(publishProjectDTO.getProjectName()+" env has not exist ");
        }
        String data = JSON.toJSONString(publishProjectDTO);
        zkClient.setData(publishProjectBasePath + publishProjectDTO.getProjectName(), data.getBytes("utf-8"));
    }


    public String getRatBaseDataZKClusterAddress() {
        return ratBaseDataZKClusterAddress;
    }

    public void setRatBaseDataZKClusterAddress(String ratBaseDataZKClusterAddress) {
        this.ratBaseDataZKClusterAddress = ratBaseDataZKClusterAddress;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(Strings.isNullOrEmpty(ratBaseDataZKClusterAddress)){
            throw new Exception(getClass().getName()+" init failed because property ratBaseDataZKClusterAddress is null ");
        }
        ZKClient client = ZKClientFactory.getZKClient(ratBaseDataZKClusterAddress);
        PublishEnvironmentZKListener publishEnvironmentZKListener =new PublishEnvironmentZKListener();
        client.addCuratorListenable(publishEnvironmentZKListener);
        client.addConnectionStateListenable(publishEnvironmentZKListener);

    }
}
