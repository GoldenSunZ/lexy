package cn.lexy.auth.controller;

import cn.lexy.auth.exception.CheckParamException;
import cn.lexy.auth.mapper.utils.PageInfo;
import cn.lexy.auth.model.Role;
import cn.lexy.auth.model.User;
import cn.lexy.auth.service.RoleService;
import cn.lexy.auth.service.UserService;
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
@RequestMapping("user")
public class UserController {

    private static final String TMPLATE = "redirect:/user/list";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("list")
    public String list(ModelMap model, @ModelAttribute HashMap<String,Object> filter,@ModelAttribute PageInfo pageInfo,@RequestParam(required = false)  String keyword){
        filter.put("page",pageInfo);
        model.addAttribute("keyword",keyword);
        if (keyword != null) {
            filter.put("keyword", keyword);
        }
        loadData(model,filter);
        return "user-mgr";
    }

    @ModelAttribute
    public void initRoles(ModelMap model){
        List<Role> list = roleService.list(null);
        model.addAttribute("roles",list);
    }

    @RequestMapping(value = "create",method = RequestMethod.POST)
    public String create( @ModelAttribute User user, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("operation","create");
        try {
            userService.create(user);
        }catch (CheckParamException ex){
            redirectAttributes.addFlashAttribute("errMsg",ex.getMessage());
        }
        return TMPLATE;
    }

    @RequestMapping("drop")
    public String drop(@RequestParam String userId,RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("operation","delete");
        try {
            userService.drop(userId);
        }catch (CheckParamException ex){
            redirectAttributes.addFlashAttribute("errMsg",ex.getMessage());
        }
        return TMPLATE;
    }

    @RequestMapping("assign-roles")
    public String assignRoles(@RequestParam String userId, @RequestParam String roles,RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("operation","assign");
        try {
            userService.assignRoles(userId, roles);
        }catch (CheckParamException ex){
            redirectAttributes.addFlashAttribute("errMsg",ex.getMessage());
        }
        return TMPLATE;
    }


    public void loadData(ModelMap model,Map<String,Object> filter){
        if(null == filter){
            filter = new HashMap<String,Object>();
        }
        Object page = filter.get("page");
        if(null == page){
            page = new PageInfo();
            filter.put("page",page);
        }
        model.addAttribute("pagination",page);
        List<User> users = userService.list(filter);
        model.addAttribute("users",users);
    }
}
