package cn.lexy.auth.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by john on 16/8/6.
 */
@Data
@EqualsAndHashCode(exclude = {"url","pid","seq","hidden","children","module"})
public class Resource implements Serializable {

    private String id;

    private String text;

    private String url;

    private String pid;

    private Integer seq;

    private Boolean hidden;

    private List<Resource> children;

    private Module module;

}
