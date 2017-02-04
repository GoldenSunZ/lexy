package cn.lexy.auth.service;

import cn.lexy.auth.mapper.ModuleMapper;
import cn.lexy.auth.mapper.ResourceMapper;
import cn.lexy.auth.model.Module;
import cn.lexy.auth.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by john on 16/8/6.
 */
@Service
public class ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    public List<Resource> list(Map<String,Object> filter){

        return resourceMapper.listByFilter(filter);
    }

    public Resource getResource(String id){
        Resource resource = resourceMapper.selectById(id);
        if(null!=resource && StringUtils.isEmpty(resource.getPid())){
            List<Resource> children = resourceMapper.getChildren(resource.getId());
            resource.setChildren(children);
        }
        return resource;

    }

    public List<Resource> getResourcesByModuleId(String moduleId) {
        if(StringUtils.isEmpty(moduleId)){
            return new ArrayList<Resource>();
        }
        return resourceMapper.getResourcesByModuleId(moduleId);
    }

    public Map<String,List<Resource>> getGroupResources(String moduleId){
        List<Module> modules = null;
        Map<String,List<Resource>> mapping = new HashMap<String, List<Resource>>();
        if(StringUtils.isEmpty(moduleId)){
            modules = moduleMapper.listByFilter(null);
        }
        if(null== modules) {
            List<Resource> listGroupResources = getListGroupResources(moduleId);
            mapping.put(moduleId,listGroupResources);
        }else{
            for (Module module : modules) {
                String id = module.getId();
                List<Resource> listGroupResources = getListGroupResources(id);
                mapping.put(id,listGroupResources);
            }
        }
        return mapping;
    }

    public List<Resource> getListGroupResources(String moduleId){
        List<Resource> root = resourceMapper.getRoot(moduleId);
        List<Resource> resources= new ArrayList<Resource>();
        if(!root.isEmpty()) {
            for (Resource resource : root) {
                String pid = resource.getId();
                List<Resource> children = resourceMapper.getChildren(pid);
                resources.add(resource);
                for (Resource child : children) {
                    resources.add(child);
                }
            }
        }else if(StringUtils.hasText(moduleId)){
            resources = resourceMapper.getResourcesByModuleId(moduleId);
        }
        return resources;
    }
}
