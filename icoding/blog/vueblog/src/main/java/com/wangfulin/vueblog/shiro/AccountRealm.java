package com.wangfulin.vueblog.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.wangfulin.vueblog.entity.User;
import com.wangfulin.vueblog.service.UserService;
import com.wangfulin.vueblog.util.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.shiro.realm.AuthorizingRealm;

/**
 * @projectName: vueblog
 * @description: AccountRealm是shiro进行登录或者权限校验的逻辑所在
 * @author: Wangfulin
 * @create: 2020-06-06 20:21
 **/
@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        // 判断是不是JwtToken
        return token instanceof JwtToken;
    }

    // 当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


    // 登陆之后，最终会委托到这个方法当中
    // 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 最后会委托给realm
        JwtToken jwtToken = (JwtToken) token;

        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();

        User user = userService.getById(Long.valueOf(userId));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }

        if (user.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定");
        }

        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);

        //
        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
    }
}
