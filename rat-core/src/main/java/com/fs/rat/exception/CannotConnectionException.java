package com.fs.rat.exception;

/**
 * Created by fanshuai on 15/6/3.
 */
public class CannotConnectionException extends Exception {
    public CannotConnectionException(String errorMsg){
        super(errorMsg);
    }
}
