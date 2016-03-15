package com.fs.rat.mng.action;

import com.alibaba.fastjson.JSON;
import com.fs.rat.mng.dto.PublishEnvironmentDTO;
import com.fs.rat.mng.dto.PublishProjectDTO;
import com.fs.rat.mng.service.PublishBaseDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by fanshuai on 15/6/17.
 */
@Controller
public class WebIndexAction {
    @Resource(name="publishBaseDataService")
    PublishBaseDataService publishBaseDataService;
    @RequestMapping(value = "/")
    public ModelAndView webIndex(){
        ModelAndView view = new ModelAndView("webIndex");
        try {
            view.addObject("envs",publishBaseDataService.getPublishEnvs());
        }catch (Exception e){

        }
        try {
            view.addObject("projects",publishBaseDataService.getPublishProjects());
        }catch (Exception e){

        }
        return view;
    }

}
