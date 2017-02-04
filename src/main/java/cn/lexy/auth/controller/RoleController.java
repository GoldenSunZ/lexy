package cn.lexy.auth.controller;

import cn.lexy.auth.exception.CheckParamException;
import cn.lexy.auth.mapper.utils.PageInfo;
import cn.lexy.auth.model.Module;
import cn.lexy.auth.model.Resource;
import cn.lexy.auth.model.Role;
import cn.lexy.auth.service.ModuleService;
import cn.lexy.auth.service.ResourceService;
import cn.lexy.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 16/8/6.
 */
@Controller
@RequestMapping("role")
public class RoleController {

    private static final String TMPLATE = "redirect:/role/list";

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ResourceService resourceService;

    @ModelAttribute
    public void initSystem(ModelMap model) {
        List<Module> systems = moduleService.list(null);
        model.addAttribute("systems", systems);
    }

    @ModelAttribute
    public void initResources(ModelMap model) {
        Map<String, List<Resource>> groupResources = resourceService.getGroupResources(null);
        model.addAttribute("resources", groupResources);
    }

    @RequestMapping("assign-resources")
    public String grantRoles(@RequestParam String moduleId, @RequestParam String roleId,
                             @RequestParam String resources, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("operation", "assign");
        String[] split = resources.split(";");
        roleService.assignResources(roleId,moduleId, split);
        return TMPLATE;
    }

    @RequestMapping("list")
    public String list(ModelMap model, @ModelAttribute HashMap<String, Object> filter, @ModelAttribute PageInfo pageInfo) {
        filter.put("page", pageInfo);
        loadData(model, filter);
        return "role-mgr";
    }

    @RequestMapping("create")
    public String add(ModelMap model, @ModelAttribute Role role, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("operation", "create");
        try {
            roleService.create(role);
        } catch (CheckParamException ex) {
            redirectAttributes.addFlashAttribute("errMsg", ex.getMessage());
        }
        return TMPLATE;
    }

    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public String update(ModelMap model, @RequestParam String name, @RequestParam String id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("operation", "modify");
        try {
            roleService.modify(new Role(id, name));
        } catch (CheckParamException ex) {
            redirectAttributes.addFlashAttribute("errMsg", ex.getMessage());
        }
        loadData(model, null);
        return TMPLATE;
    }

    @RequestMapping("drop")
    public String drop(ModelMap model, @RequestParam String id,RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("operation", "delete");
        try {
            roleService.drop(id);
        }catch (CheckParamException ex){
            redirectAttributes.addFlashAttribute("errMsg",ex.getMessage());
        }
        loadData(model, null);
        return TMPLATE;
    }

    private void loadData(ModelMap model, Map<String, Object> filter) {
        if (null == filter) {
            filter = new HashMap<String, Object>();
        }
        Object page = filter.get("page");
        if (null == page) {
            page = new PageInfo();
            filter.put("page", page);
        }
        model.addAttribute("pagination", page);
        List<Role> list = roleService.list(filter);

        model.addAttribute("roles", list);
    }
}
