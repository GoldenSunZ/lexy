package cn.lexy.auth.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 16/8/6.
 */
@Data
public class User implements Serializable {

    private String id;

    private String username;

    private String password;

    private String nickname;

    private String orgId;

    private Boolean isBuiltIn;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    private List<Role> roles;

    public String getAssignedRoles(){
        if(null == roles){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Role role : roles) {
            if(i>0){
                sb.append(";");
            }
            sb.append(role.getId());
            i++;
        }
        return sb.toString();
    }
}
