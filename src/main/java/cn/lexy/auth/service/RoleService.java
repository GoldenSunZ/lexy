package cn.lexy.auth.service;

import cn.lexy.auth.exception.CheckParamException;
import cn.lexy.auth.mapper.RoleMapper;
import cn.lexy.auth.mapper.utils.ParamUtils;
import cn.lexy.auth.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by john on 16/8/6.
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public List<Role> list(Map<String, Object> filter) {
        Map<String, Object> map = ParamUtils.filterParams(filter);
        List<Role> roles = roleMapper.listByFilter(map);
        return roles;
    }

    public void modify(Role role) {
        if (null != role) {
            if (StringUtils.isEmpty(role.getId())) {
                throw new CheckParamException("缺少角色ID,不能更新");
            }
            if (StringUtils.hasText(role.getName()) || StringUtils.hasText(role.getDescription())) {
                checkNameRepeat(role.getName());
                roleMapper.updateById(role);
            }
        }
    }

    public void drop(String id) {
        if (StringUtils.hasText(id)) {
            Role role1 = roleMapper.selectById(id);
            if (null == role1) {
                return;
            }
            if (null != role1.getIsBuiltIn() && role1.getIsBuiltIn()) {
                throw new CheckParamException("内部帐户不能删除");
            }
        }
        roleMapper.delete(id);
    }

    public void create(Role role) {
        if (null != role) {
            if (StringUtils.isEmpty(role.getName())) {
                throw new CheckParamException("角色名称不能为空");
            }
            //名字去重
            checkNameRepeat(role.getName());
            role.setIsBuiltIn(false);
            role.setId(ParamUtils.getIDFromUUID());
            roleMapper.insert(role);
        }
    }


    private void checkNameRepeat(String name) {
        if (StringUtils.hasText(name)) {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("name", name);
            List<Role> roles = roleMapper.listByFilter(filter);
            if (!roles.isEmpty()) {
                throw new CheckParamException("角色名字重复");
            }
        }
    }

    @Transactional
    public void assignResources(String roleId,String moduleId, String[] split) {
        if(StringUtils.isEmpty(roleId)||null == split || split.length==0||StringUtils.isEmpty(moduleId)){
            return;
        }
        roleMapper.clearResources(roleId,moduleId);
        roleMapper.assignResources(roleId,split);
    }
}
