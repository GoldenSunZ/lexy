package cn.lexy.auth.service;

import cn.lexy.auth.mapper.ModuleMapper;
import cn.lexy.auth.model.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by john on 16/8/6.
 */
@Service
public class ModuleService {

    @Autowired
    private ModuleMapper moduleMapper;

    public List<Module> list(Map<String,Object> filter){
        List<Module> modules = moduleMapper.listByFilter(filter);
        return modules;
    }
}
