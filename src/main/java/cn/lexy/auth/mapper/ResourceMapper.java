package cn.lexy.auth.mapper;

import cn.lexy.auth.mapper.utils.CrudTemplate;
import cn.lexy.auth.model.Resource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by john on 16/8/6.
 */
public interface ResourceMapper extends CrudTemplate<Resource> {

    List<Resource> getResourcesByRoleId(@Param("roleId") String roleId);

    List<Resource> getResourcesByModuleId(@Param("moduleId")String moduleId);

    List<Resource> getRoot(@Param("moduleId") String moduleId);

    List<Resource> getChildren(@Param("pid") String pid);
}
