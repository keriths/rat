package com.fs.rat.mng.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fs.rat.mng.sysinit.SystemInit;

/**
 * Created by fanshuai on 15/6/17.
 */
public class AjaxResult {
    private boolean isSuccess = true;
    private String errorMsg = "";
    private JSONObject data = new JSONObject();
    public void addValue(String key,Object value){
        this.data.put(key,value);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
    public static void main(String[] args){
        System.out.println(JSON.toJSONString(new AjaxResult()));
    }
}
