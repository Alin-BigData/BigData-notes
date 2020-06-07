package com.wangfulin.vueblog.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * @projectName: vueblog
 * @description: 封装用户信息 可公开的信息
 * @author: Wangfulin
 * @create: 2020-06-06 22:14
 **/
@Data
public class AccountProfile implements Serializable {

    private Long id;

    private String username;

    private String avatar;

    private String email;

}
