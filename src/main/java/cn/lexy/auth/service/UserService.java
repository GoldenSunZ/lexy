package cn.lexy.auth.service;

import cn.lexy.auth.config.LexyAuthConstants;
import cn.lexy.auth.exception.CheckParamException;
import cn.lexy.auth.mapper.UserMapper;
import cn.lexy.auth.mapper.utils.ParamUtils;
import cn.lexy.auth.model.User;
import cn.lexy.auth.page.MenuPage;
import cn.lexy.auth.page.UserPage;
import cn.lexy.auth.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by john on 16/8/7.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> list(Map<String, Object> filter) {
        Map<String, Object> map = ParamUtils.filterParams(filter);
        List<User> users = userMapper.listByFilter(map);
        return users;
    }

    public void create(User user) {
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new CheckParamException("用户名不能为空");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword("123");
        }
        Map<String, Object> filter = new HashMap();
        filter.put("username", user.getUsername());
        List<User> users = userMapper.listByFilter(filter);
        if (!users.isEmpty()) {
            throw new CheckParamException("该用户名已经存在");
        }
        user.setPassword(user.getPassword().trim());
        user.setUsername(user.getUsername().trim());
        user.setId(ParamUtils.getIDFromUUID());
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userMapper.insert(user);
    }

    @Transactional
    public void assignRoles(String userId, String roles) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(roles)) {
            return;
        }
        userMapper.clearRoles(userId);
        String[] split = roles.split(";");
        userMapper.assignRoles(userId, split);
    }

    public void drop(String userId) {
        if (StringUtils.hasText(userId)) {
            User user = userMapper.selectById(userId);
            if (null != user) {
                if (null != user.getIsBuiltIn() && user.getIsBuiltIn()) {
                    throw new CheckParamException("系统内建用户不能删除");
                }
                userMapper.delete(userId);
            }
        }
    }

    public UserPage getUserForLogin(String name, String pwd){
        UserPage user = new UserPage();
        user.setUsername(name);
        user.setPassword(pwd);
        user = userMapper.getUserForLogin(user);
        if (null!=user) {
            List<String> authMenuUrls = userMapper.listResourceUrlListByUserIdAndModuleId(user.getId(), LexyAuthConstants.AUTH_MODULE);
            List<String> authUrls = new ArrayList<String>();
            if (authMenuUrls!=null && !authMenuUrls.isEmpty()) {
                for (String url : authMenuUrls) {
                    authUrls.add(url.substring(0, url.lastIndexOf("/")));
                }
                List<MenuPage> rootMenus = userMapper.listRootMenuTreeByAuthUrls(authMenuUrls);
                for (MenuPage m : rootMenus) {
                    List<MenuPage> subMenus = userMapper.listSubMenuTreeByAuthUrls(m.getId(), authMenuUrls);
                    m.setChildren(subMenus);
                }
                user.setAuthorizedMenus(rootMenus);
                user.setAuthorizedUrls(authUrls);
                if(null == user.getNickname() || "".equals(user.getNickname())){
                    user.setNickname(user.getUsername());
                }
            }
        }
        return user;
    }

    public boolean updpsw(UserPage userPage) {
        userPage.setPassword(MD5.getMd5(userPage.getPassword()));
        UserPage user = userMapper.getUserForLogin(userPage);
        if (user==null) {
            return false;
        } else {
            userMapper.updpsw(userPage.getId(), MD5.getMd5(userPage.getNewpsw()));
            return true;
        }
    }
}
