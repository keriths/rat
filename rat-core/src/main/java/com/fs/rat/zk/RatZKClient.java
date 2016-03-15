package com.fs.rat.zk;

import com.alibaba.fastjson.JSON;
import com.fs.rat.client.RatPropertyClient;
import com.fs.rat.exception.CannotConnectionException;
import com.fs.rat.exception.ZKClientInitException;
import com.fs.rat.exception.ZKException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanshuai on 15/6/2.
 */
public class RatZKClient implements RatPropertyClient {
    public static final String propertyBaseZKNode ="/configcenter/";
    private ZKClient zkClient ;
    private String zkClusterHosts = "192.168.120.130:2182";
    //MAP<domain,Map<key,val>>
    private Map<String,Map<String,PropertyValueEntity>> propertyValues = new HashMap<String, Map<String, PropertyValueEntity>>();
    public RatZKClient(String zkClusterHosts) throws ZKClientInitException {
        this.zkClusterHosts = zkClusterHosts;
        this.zkClient = new ZKClient(zkClusterHosts,new RatClientCuratorListener(this),new RatClientConnectionStateListener(this));
        this.zkClient.init();
    }
    public RatZKClient() throws ZKClientInitException {
        this.zkClient = new ZKClient(zkClusterHosts,new RatClientCuratorListener(this),new RatClientConnectionStateListener(this));
        this.zkClient.init();
    }

    public static Map<String,String> notExistsNode = new HashMap<String, String>();
    //public static List<String> notExistsNode = new ArrayList<String>();
    private PropertyValueEntity getPropertyValueFromZK(String domain,String key,CuratorWatcher watcher){
        try {
            String ratDomainNode = propertyBaseZKNode+domain;
            String ratDomainKeyNode=propertyBaseZKNode+domain+"/"+key;
            if(zkClient.existsNode(ratDomainKeyNode)){
                byte[] datas = zkClient.getNodeValue(ratDomainKeyNode, watcher);
                if(datas==null){
                    return null;
                }
                String strData = new String(datas,"UTF-8");
                JSON json = JSON.parseObject(strData);
                return JSON.toJavaObject(json,PropertyValueEntity.class);
            }
            notExistsNode.put(ratDomainKeyNode,ratDomainNode);
            if(zkClient.existsNode(ratDomainNode)){
                return null;
            }
            if(zkClient.existsNode(propertyBaseZKNode)){
                return null;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private PropertyValueEntity getPropertyValueFromCache(String domain,String key){
        Map<String,PropertyValueEntity> domainProperties = propertyValues.get(domain);
        if(domainProperties==null){
            return null;
        }
        return domainProperties.get(key);
    }
    public void setPropertyValueToCache(PropertyValueEntity valueEntity){
        Map<String,PropertyValueEntity> domainProperties = propertyValues.get(valueEntity.getDomain());
        if(domainProperties==null){
            domainProperties = new HashMap<String, PropertyValueEntity>();
            propertyValues.put(valueEntity.getDomain(),domainProperties);
        }
        domainProperties.put(valueEntity.getKey(),valueEntity);
    }






    public static void main(String[] args){
        PropertyValueEntity p = new PropertyValueEntity();
        p.setDomain("domain");
        p.setKey("testname6");
        p.setValue("55556");
        try {
            new RatZKClient().setPropertyValue(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPropertyValue(PropertyValueEntity propertyValueEntity) throws Exception{
        String domain = propertyValueEntity.getDomain();
        String key = propertyValueEntity.getKey();
        String propertyNodePath=propertyBaseZKNode+domain+"/"+key;
        String jsonValue = JSON.toJSONString(propertyValueEntity);
        zkClient.setData(propertyNodePath,jsonValue.getBytes("UTF-8"));
    }

    @Override
    public String getPropertyValue(String key) {
        if(key==null){
            return null;
        }
        if(key.indexOf(".")==-1){
            return null;
        }
        String domain = key.substring(0,key.indexOf("."));
        String propertyKey = key.substring(key.indexOf(".")+1);
        PropertyValueEntity propertyValueEntity = getPropertyValueFromCache(domain, propertyKey);
        if(propertyValueEntity!=null){
            return propertyValueEntity.getValue();
        }
        propertyValueEntity = getPropertyValueFromZK(domain,propertyKey,null);
        if(propertyValueEntity==null){
            propertyValueEntity = new PropertyValueEntity();
            propertyValueEntity.setDomain(domain);
            propertyValueEntity.setKey(propertyKey);
        }
        setPropertyValueToCache(propertyValueEntity);
        return propertyValueEntity.getValue();
    }

    @Override
    public String getPropertyValue(String key, String defaultVal) {
        if(key==null){
            return null;
        }
        String value = getPropertyValue(key);
        return value==null?defaultVal:value;
    }

    public void flushCachedPropertyValue() {
        for (Map.Entry<String,Map<String,PropertyValueEntity>> domainEntry:propertyValues.entrySet()){
            String domain = domainEntry.getKey();
            Map<String,PropertyValueEntity> domainProperties = domainEntry.getValue();
            for (Map.Entry<String,PropertyValueEntity> keyEntry:domainProperties.entrySet()){
                try {
                    String key = keyEntry.getKey();
                    PropertyValueEntity newValue = getPropertyValueFromZK(domain, key,null);
                    if(newValue==null){
                        newValue = new PropertyValueEntity();
                        newValue.setDomain(domain);
                        newValue.setKey(key);
                    }
                    setPropertyValueToCache(newValue);
                }catch (Exception e){

                }
            }
        }
    }
}

