package cn.lexy.auth.page;

import cn.lexy.auth.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by zhouxiaobo on 16/8/11.
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPage extends User {
    private String depName;
    private List<String> authorizedUrls;
    private List<MenuPage> authorizedMenus;
    private String newpsw;

    public String getDepName() {
        return depName;
    }
    public void setDepName(String depName) {
        this.depName = depName;
    }
    public List<String> getAuthorizedUrls() {
        return authorizedUrls;
    }
    public void setAuthorizedUrls(List<String> authorizedUrls) {
        this.authorizedUrls = authorizedUrls;
    }
    public List<MenuPage> getAuthorizedMenus() {
        return authorizedMenus;
    }
    public void setAuthorizedMenus(List<MenuPage> authorizedMenus) {
        this.authorizedMenus = authorizedMenus;
    }
    public String getNewpsw() {
        return newpsw;
    }
    public void setNewpsw(String newpsw) {
        this.newpsw = newpsw;
    }

}

