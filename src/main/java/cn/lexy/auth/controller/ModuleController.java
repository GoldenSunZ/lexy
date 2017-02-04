package cn.lexy.auth.controller;

import cn.lexy.auth.mapper.utils.PageInfo;
import cn.lexy.auth.model.Module;
import cn.lexy.auth.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 16/8/6.
 */
@Controller
@RequestMapping("module")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @RequestMapping("list")
    public String list(ModelMap model, @ModelAttribute PageInfo pageInfo){
        Map<String,Object> filter = new HashMap<String,Object>();
        filter.put("page",pageInfo);
        List<Module> list = moduleService.list(filter);
        model.addAttribute("systems",list);
        model.addAttribute("pagination",pageInfo);
        return "sys-mgr";
    }

}
