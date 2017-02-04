package cn.lexy.auth.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by john on 16/8/6.
 */
@Data
public class Module implements Serializable {

    private String id;

    private String name;

    private Boolean hidden;

    private Set<Resource> resources;
}
