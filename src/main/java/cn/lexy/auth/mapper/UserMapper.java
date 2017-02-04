package cn.lexy.auth.mapper;

import cn.lexy.auth.mapper.utils.CrudTemplate;
import cn.lexy.auth.model.User;
import cn.lexy.auth.page.MenuPage;
import cn.lexy.auth.page.UserPage;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by john on 16/8/6.
 */
public interface UserMapper extends CrudTemplate<User> {

    void clearRoles(@Param("userId") String userId);

    void assignRoles(@Param("userId") String userId, @Param("roles") String[] roles);

    public UserPage getUserForLogin(User user);

    public List<String> listResourceUrlListByUserId(String userId);

    public List<MenuPage> listRootMenuTreeByAuthUrls(List<String> authUrls);

    public List<MenuPage> listSubMenuTreeByAuthUrls(String pid, List<String> authUrls);

    public int updpsw(String userId, String newpsw);
    
    public List<String> listResourceUrlListByUserIdAndModuleId(String userId, String moduelId);
}
