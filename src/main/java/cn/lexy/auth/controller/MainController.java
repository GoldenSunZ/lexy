package cn.lexy.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by john on 16/8/2.
 */
@Controller
public class MainController {

    @RequestMapping(value = "say",method = RequestMethod.GET)
    @ResponseBody
    public String hello(String words){
        return "Say - >"+words;
    }

    @RequestMapping(value = {"/","role-mgr"})
    public String home(ModelMap model){
        return "redirect:/role/list";
    }


    @RequestMapping("/res-mgr")
    public String res(ModelMap model){
        return "redirect:/res/list";
    }

    @RequestMapping("/sys-mgr")
    public String sys(ModelMap model){
        return "redirect:/module/list";
    }

    @RequestMapping("/user-mgr")
    public String user(ModelMap model){
        return "redirect:/user/list";
    }
}
