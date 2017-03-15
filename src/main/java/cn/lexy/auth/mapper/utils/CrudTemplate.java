package cn.lexy.auth.mapper.utils;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by john on 16/8/6.
 */
public interface CrudTemplate<T> {

    List<T> listByFilter(Map filter);

    T selectById(@Param("id") String id);

    void updateById(T module);

    void insert(T module);

    void delete(@Param("id") String id);
}
