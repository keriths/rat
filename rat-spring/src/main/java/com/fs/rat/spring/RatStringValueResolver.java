package com.fs.rat.spring;

import com.fs.rat.client.RatPropertyClientFactory;
import com.fs.rat.exception.CannotConnectionException;
import org.springframework.util.StringValueResolver;

import java.util.Properties;

/**
 * Created by fanshuai on 15/6/12.
 */
public class RatStringValueResolver implements StringValueResolver {
    private String prefix="${";
    private String suffix = "}";
    private Properties props;
    public RatStringValueResolver(String prefix,String suffix,Properties props){
        this.prefix = prefix;
        this.suffix = suffix;
        this.props=props;
    }
    @Override
    public String resolveStringValue(String value){
        if(!checkCanReset(value)){
            return value;
        }
        try {
            String valueKey = getRatPropertiesKey(value);
            String newString = RatPropertyClientFactory.getDefaultRatClient().getPropertyValue(valueKey);
            if(newString==null&&props!=null){
                newString = props.getProperty(valueKey);
            }
            if(newString==null){
                throw new Exception(value+" setting is null");
            }
            value = newString;
        } catch (CannotConnectionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return value;
    }
    private String getRatPropertiesKey(String value){
        return value.substring(value.indexOf(prefix)+prefix.length(),value.indexOf(suffix));
    }
    private boolean checkCanReset(String value){
        if(!value.startsWith(prefix)){
            return false;
        }
        if(!value.endsWith(suffix)){
            return false;
        }
        return true;
    }
}
