package cn.lexy.auth.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by john on 16/8/6.
 */
@Data
public class Role implements Serializable {

    private String id;

    private String name;

    private String description;

    private Boolean isBuiltIn;

    private Set<Resource> grantedResources;

    public Role(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role() {
    }

    public Role(String id) {
        this.id = id;
    }

    public String getAssignedResources(){
        if(null == grantedResources || grantedResources.isEmpty()){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Resource grantedResource : grantedResources) {
            if(i!=0){
                sb.append(";");
            }
            sb.append(grantedResource.getId());
            i++;
        }
        return sb.toString();
    }
}
