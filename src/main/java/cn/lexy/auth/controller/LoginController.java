package cn.lexy.auth.controller;

import cn.lexy.auth.page.MenuPage;
import cn.lexy.auth.page.UserPage;
import cn.lexy.auth.service.UserService;
import cn.lexy.auth.util.Constants;
import cn.lexy.auth.util.Json;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import cn.lexy.auth.util.MD5;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/login","/",""})
public class LoginController {
	
	private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
    public String welcome() {
        return "login";
    }

    @RequestMapping(value="/doLogin", method = RequestMethod.POST)
    public String loginValidation(UserPage userPage, ModelMap modelMap, HttpServletRequest request) {
        boolean loginSuccess = false;
        String name = userPage.getUsername();
        String pwd = userPage.getPassword();

        if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(pwd)){
			UserPage user = userService.getUserForLogin(name, MD5.getMd5(pwd));
			if(null != user){
				Map<String, String> menus = new HashMap<String, String>();
				if (user.getAuthorizedMenus()!=null && !user.getAuthorizedMenus().isEmpty()) {
					loginSuccess = true;
					for (MenuPage pm : user.getAuthorizedMenus()) {
						for (MenuPage m : pm.getChildren()) {
							menus.put(m.getUrl(), m.getPid());
						}
					}
					request.getSession().setAttribute(Constants.SESSION_MENUS, menus);
					request.getSession().setAttribute(Constants.SESSION_USER, user);
				} else {
					modelMap.put("loginError", true);
		            modelMap.put("errorMsg", "您没有权限访问本系统！");
		            return "login";
				}
			}
        }

        if(loginSuccess){
            return "redirect:/user/list";
        }else{
            modelMap.put("loginError", true);
            modelMap.put("errorMsg", "用户名或密码错误！");
            return "login";
        }
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String welcome(HttpServletRequest request, ModelMap model) {
        if (request.getSession().getAttribute(Constants.SESSION_USER)!=null) {
            request.getSession().removeAttribute(Constants.SESSION_USER);
        }
        return "redirect:/login";
    }

    @RequestMapping(value="/updpsw", method = RequestMethod.POST)
    @ResponseBody
    public Json updpsw(String newpassword, String confirmpassword, String oldpassword,  HttpServletRequest request) {
        Json jsonObject = new Json();
        if(newpassword.equalsIgnoreCase(confirmpassword)){
            UserPage userPage = new UserPage();
            userPage.setUsername(((UserPage) request.getSession().getAttribute(Constants.SESSION_USER)).getUsername());
            userPage.setId(((UserPage)request.getSession().getAttribute(Constants.SESSION_USER)).getId());
            userPage.setPassword(oldpassword);
            userPage.setNewpsw(newpassword);
            boolean result = userService.updpsw(userPage);
            if (result){
                jsonObject.setSuccess(true);
            }else{
                jsonObject.setMsg("旧密码输入错误.");
                jsonObject.setSuccess(false);
            }
        }else{
            jsonObject.setMsg("密码输入不一致.");
            jsonObject.setSuccess(false);
        }
        return jsonObject;
    }
}

