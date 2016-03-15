package com.fs.demo.rat.test.action;

import com.fs.demo.rat.test.service.TestService;
import com.fs.rat.client.RatPropertyClientFactory;
import com.fs.rat.exception.ZKClientInitException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by fanshuai on 15/6/11.
 */
@Controller
public class TestAction {
    @Resource(name = "testService")
    TestService testService;

    @Resource(name = "tttt")
    TestService tttt;

    @RequestMapping(value = "/")
    @ResponseBody
    public Object index(){
        String aaa = null;
        try {
            aaa = RatPropertyClientFactory.getDefaultRatClient().getPropertyValue("domain.testname6");
        } catch (ZKClientInitException e) {
            e.printStackTrace();
        }
        return aaa+"   "+testService.getName()+"  "+ testService.getValueName()+"  ttttt---"+tttt.getName()+"  "+ tttt.getValueName();
    }

}
