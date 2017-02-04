package cn.lexy.auth.page;

import cn.lexy.auth.model.Menu;

import java.util.List;

/**
 * Created by zhouxiaobo on 16/8/11.
 */
@SuppressWarnings("serial")
public class MenuPage extends Menu {
    private String ids;
    private List<MenuPage> children;

    public String getIds() {
        return ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
    }
    public List<MenuPage> getChildren() {
        return children;
    }
    public void setChildren(List<MenuPage> children) {
        this.children = children;
    }
}
