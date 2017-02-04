package cn.lexy.auth.controller;

import cn.lexy.auth.model.Module;
import cn.lexy.auth.model.Resource;
import cn.lexy.auth.service.ModuleService;
import cn.lexy.auth.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by john on 16/8/6.
 */
@Controller
@RequestMapping("/res")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ModuleService moduleService;

    @RequestMapping("list")
    public String list(ModelMap model,@RequestParam(required = false,defaultValue = "-1") String moduleId){
        List<Module> systems = moduleService.list(null);
        model.addAttribute("systems",systems);
        if(moduleId.equals("-1")&&!systems.isEmpty()){
            moduleId = systems.get(0).getId();
        }
        List<Resource> resources = resourceService.getListGroupResources(moduleId);
        model.addAttribute("resources",resources);
        model.addAttribute("moduleId",moduleId);
        loadData(model,null);
        return "res-mgr";
    }

    private Map<String,List<Resource>> groupResources(ModelMap model){
        return resourceService.getGroupResources(null);
    }

    private void loadData(ModelMap model,Map<String,Object> filter){

    }
}
