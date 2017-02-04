package cn.lexy.auth.mapper;

import cn.lexy.auth.mapper.utils.CrudTemplate;
import cn.lexy.auth.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by john on 16/8/6.
 */
public interface RoleMapper extends CrudTemplate<Role> {

    List<Role> getRolesByUserId(@Param("userId") String userId);

    //给role分配 资源
    void assignResources(@Param("roleId") String roleId, @Param("resources") String[] split);

    //清除和role关联的资源
    void clearResources(@Param("roleId") String roleId,@Param("moduleId") String moduleId);
}
