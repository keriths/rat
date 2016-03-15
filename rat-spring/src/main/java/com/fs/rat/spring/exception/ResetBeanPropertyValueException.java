package com.fs.rat.spring.exception;

import org.springframework.beans.BeansException;

/**
 * Created by fanshuai on 15/6/11.
 */
public class ResetBeanPropertyValueException extends BeansException {
    public ResetBeanPropertyValueException(String msg,Throwable t){
        super(msg,t);
    }
    public ResetBeanPropertyValueException(String msg){
        super(msg);
    }
}
