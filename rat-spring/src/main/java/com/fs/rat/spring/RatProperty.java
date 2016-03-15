package com.fs.rat.spring;

import com.fs.rat.client.RatPropertyClientFactory;
import com.fs.rat.exception.CannotConnectionException;
import com.fs.rat.exception.ZKClientInitException;
import com.fs.rat.spring.exception.PropertyFileNotFoundException;
import com.fs.rat.spring.exception.ResetBeanPropertyValueException;
import com.sun.org.glassfish.external.statistics.annotations.Reset;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.core.io.Resource;
import org.springframework.util.StringValueResolver;

import java.io.IOException;
import java.util.*;

/**
 * Created by fanshuai on 15/6/11.
 */
public class RatProperty implements BeanFactoryPostProcessor {
    private Resource[] locations;
    private Properties props;
    private final Logger log = Logger.getLogger(getClass());
    private String prefix="${";
    private String suffix = "}";
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String bname=null;
        String pname=null;
        try {
            initRatPropertieFiles();//兼容文件配置方式，文件中有的全记录下来
            initRatZKClient();
            StringValueResolver stringValueResolver=new RatStringValueResolver(prefix,suffix,props);
            String [] beanNames = beanFactory.getBeanDefinitionNames();
            for (String beanName:beanNames){
                bname = beanName;
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
                for(PropertyValue propertyValue : propertyValues.getPropertyValues()){
                    log.info("beanName:"+beanName + " propertyName:" + propertyValue.getName() + " valueType:" + propertyValue.getValue().getClass().getName());
                    pname = propertyValue.getName();
                    propertyValues.add(propertyValue.getName(),reSetValue(propertyValue.getValue(),stringValueResolver));
                }
            }
            beanFactory.resolveAliases(stringValueResolver);
            beanFactory.addEmbeddedValueResolver(stringValueResolver);
        }catch (Exception e){
            throw new ResetBeanPropertyValueException(" reset beanname("+bname+") property("+pname+") value exception:"+e.getMessage(),e);
        }
    }

    private void initRatPropertieFiles() throws PropertyFileNotFoundException {
        if(locations==null){
            return ;
        }
        for (Resource resource : locations ){
            if(!resource.exists()){
               throw new PropertyFileNotFoundException();
            }
            try {

                props.load(resource.getInputStream());
            } catch (IOException e) {
                throw new PropertyFileNotFoundException();
            }

        }
    }

    private void initRatZKClient() throws ZKClientInitException {
        RatPropertyClientFactory.getDefaultRatClient();
    }

    public Object reSetValue(Object value,StringValueResolver stringValueResolver) throws Exception{
        if(value instanceof String){
            return mergeStringValue((String) value, stringValueResolver);
        }
        if(value instanceof TypedStringValue){
            return mergeTypedStringValue((TypedStringValue)value,stringValueResolver);
        }
        if(value instanceof Set){
            return mergeSetValue((Set)value,stringValueResolver);
        }
        if(value instanceof List){
            return mergeListValue((List)value,stringValueResolver);
        }
        if(value instanceof Map){
            return mergeMapValue((Map)value,stringValueResolver);
        }
        if (value instanceof Object[]){
            return mergeArrayValue((Object[])value,stringValueResolver);
        }
        return value;
    }

    private Object mergeArrayValue(Object[] arrayVal,StringValueResolver stringValueResolver)throws Exception {
        for (int i = 0;i<arrayVal.length;i++){
            arrayVal[i]=reSetValue(arrayVal[i],stringValueResolver);
        }
        return arrayVal;
    }

    private Object mergeMapValue(Map mapVal,StringValueResolver stringValueResolver) throws Exception{
        Map newMapVal = new LinkedHashMap();

        Set<Map.Entry> entries = mapVal.entrySet();
        for (Map.Entry<Object,Object> entry:entries){
            newMapVal.put(entry.getKey(),reSetValue(entry.getValue(),stringValueResolver));
        }

        mapVal.clear();
        mapVal.putAll(newMapVal);
        return mapVal;
    }

    private Object mergeListValue(List listVal,StringValueResolver stringValueResolver) throws Exception{
        for(int i =0;i<listVal.size();i++){
            Object value = listVal.get(i);
            listVal.set(i,reSetValue(value,stringValueResolver));
        }
        return listVal;
    }

    private Object mergeSetValue(Set setVal,StringValueResolver stringValueResolver) throws Exception{
        Set newSetVal = new LinkedHashSet();
        for(Object value:setVal){
            newSetVal.add(reSetValue(value,stringValueResolver));
        }
        setVal.clear();
        setVal.addAll(newSetVal);
        return setVal;
    }

    private Object mergeTypedStringValue(TypedStringValue value,StringValueResolver stringValueResolver) throws Exception {
        value.setValue(mergeStringValue(value.getValue(),stringValueResolver));
        return value;
    }

    private String mergeStringValue(String value,StringValueResolver stringValueResolver) throws Exception{
        return stringValueResolver.resolveStringValue(value);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setLocations(Resource[] locations) {
        this.locations = locations;
    }
}
