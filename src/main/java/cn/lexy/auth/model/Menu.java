package cn.lexy.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zhouxiaobo on 16/8/11.
 */
@Data
public class Menu implements Serializable {
    private String id;

    private String text;

    private String url;

    private String pid;

    private Integer seq;
}
