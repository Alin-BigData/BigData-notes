package com.wangfulin.vueblog.shiro;

import cn.hutool.json.JSONUtil;
import com.wangfulin.vueblog.common.Result;
import com.wangfulin.vueblog.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @projectName: vueblog
 * @description: 定义jwt的过滤器JwtFilter
 * 继承的是Shiro内置的AuthenticatingFilter，
 * 一个内置了可以自动登录方法的的过滤器，
 * 也可以继承BasicHttpAuthenticationFilter。
 * <p>
 * 参考：https://juejin.im/post/59f1b2766fb9a0450e755993
 * https://www.jianshu.com/p/0b1131be7ace
 * @author: Wangfulin
 * @create: 2020-06-06 20:33
 **/
@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;

    /**
     * @Name: createToken
     * @Description: 实现登录，生成自定义的JwtToken
     * @Param: [servletRequest, servletResponse]
     * @return: org.apache.shiro.authc.AuthenticationToken
     * @Author: Wangfulin
     * @Date: 2020/6/6
     */
    // 用户登陆完成，将jwt返回给用户，用户访问时，jwt放在用户的header里面
    // 在调用executeLogin的时候，会调用
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取 token
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)) {
            // 不带有jwt
            return null;
        }
        // 封装成token的形式
        return new JwtToken(jwt);
    }


    /**
     * @Name: onAccessDenied
     * @Description: 拦截校验，当头部没有Authorization时候，
     * 我们直接通过，不需要自动登录；当带有的时候，
     * 首先我们校验jwt的有效性，
     * 没问题我们就直接执行executeLogin方法实现自动登录
     * @Param: [servletRequest, servletResponse]
     * @return: boolean
     * @Author: Wangfulin
     * @Date: 2020/6/6
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");

        if (StringUtils.isEmpty(jwt)) {
            return true;
        } else { // 有jwt
            // 校验jwt
            Claims claim = jwtUtils.getClaimByToken(jwt);
            // 判断是否已过期
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }

        }
        // 执行登陆处理 登陆成功会到达onLoginSuccess处理
        return executeLogin(servletRequest, servletResponse);
    }


    /**
     * @Name: onLoginFailure
     * @Description: 登陆异常时抛出异常
     * @Param: [token, e, request, response]
     * @return: boolean
     * @Author: Wangfulin
     * @Date: 2020/6/6
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Throwable throwable = e.getCause() == null ? e : e.getCause();
        // 错误原因抛出
        Result result = Result.fail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        return false;
    }

    /**
     * @Name: preHandle
     * @Description: 拦截器的前置拦截
     * 项目中除了需要跨域全局配置之外，
     * 我们再拦截器中也需要提供跨域支持。
     * 这样，拦截器才不会在进入Controller之前就被限制了。
     * @Param: [request, response]
     * @return: boolean
     * @Author: Wangfulin
     * @Date: 2020/6/6
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
