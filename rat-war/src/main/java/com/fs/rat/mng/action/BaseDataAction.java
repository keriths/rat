package com.fs.rat.mng.action;

import com.fs.rat.mng.dto.PublishEnvironmentDTO;
import com.fs.rat.mng.dto.PublishProjectDTO;
import com.fs.rat.mng.service.PublishBaseDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by fanshuai on 15/6/17.
 */
@Controller
public class BaseDataAction {
    @Resource(name="publishBaseDataService")
    PublishBaseDataService publishBaseDataService;
    @RequestMapping(value = "/env/add")
    public Object addEnv(PublishEnvironmentDTO environmentDTO){
        AjaxResult result = new AjaxResult();
        try {
            publishBaseDataService.addPublishEnv(environmentDTO);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/env/edit")
    public Object editEnv(PublishEnvironmentDTO environmentDTO){
        AjaxResult result = new AjaxResult();
        try {
            publishBaseDataService.editPublishEnv(environmentDTO);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/project/add")
    public Object addEnv(PublishProjectDTO projectDTO){
        AjaxResult result = new AjaxResult();
        try {
            publishBaseDataService.addPublishProject(projectDTO);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/project/edit")
    public Object editEnv(PublishProjectDTO projectDTO){
        AjaxResult result = new AjaxResult();
        try {
            publishBaseDataService.editPublishProject(projectDTO);
        }catch (Exception e){
            result.setSuccess(false);
            result.setErrorMsg(e.getMessage());
        }
        return result;
    }
}
