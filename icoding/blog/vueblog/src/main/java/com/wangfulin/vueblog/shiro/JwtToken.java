package com.wangfulin.vueblog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @projectName: vueblog
 * @description:
 * shiro默认supports的
 * 是UsernamePasswordToken，
 * 而我们现在采用了jwt的方式，
 * 所以这里我们自定义一个JwtToken，
 * 来完成shiro的supports方法。
 * @author: Wangfulin
 * @create: 2020-06-06 20:39
 **/


public class JwtToken implements AuthenticationToken {
    // 密钥
    private String token;

    public JwtToken(String jwt) {
        this.token = jwt;
    }

    // 通过principal来识别唯一的用户
    @Override
    public Object getPrincipal() {
        return token;
    }

    // 证书
    @Override
    public Object getCredentials() {
        return token;
    }
}
