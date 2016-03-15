package com.fs.demo.rat.test.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fanshuai on 15/6/11.
 */
@Service("tttt")
public class TestService {

    private String name;
    @Value("${domain.testname1}")
    private String valueName;
    private List<String> listNames;
    private Set<String> setNames ;
    private Map<String,String > mapNames;
    private String[] arrayNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getListNames() {
        return listNames;
    }

    public void setListNames(List<String> listNames) {
        this.listNames = listNames;
    }

    public Set<String> getSetNames() {
        return setNames;
    }

    public void setSetNames(Set<String> setNames) {
        this.setNames = setNames;
    }

    public Map<String, String> getMapNames() {
        return mapNames;
    }

    public void setMapNames(Map<String, String> mapNames) {
        this.mapNames = mapNames;
    }

    public String[] getArrayNames() {
        return arrayNames;
    }

    public void setArrayNames(String[] arrayNames) {
        this.arrayNames = arrayNames;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
}
