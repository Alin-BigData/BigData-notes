package com.wangfulin.vueblog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangfulin.vueblog.common.Result;
import com.wangfulin.vueblog.dto.LoginDto;
import com.wangfulin.vueblog.entity.User;
import com.wangfulin.vueblog.service.UserService;


import com.wangfulin.vueblog.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @projectName: vueblog
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-06-08 13:40
 **/
@RestController
@RequestMapping("/account")
public class AccountController {



    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/index")
    public Result inde() {
        User user = userService.getById(1L);

        return Result.success(200,"ok",user);
    }
    /**
     * 默认账号密码：markerhub / 111111
     */
    @CrossOrigin//跨域
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");

        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
            return Result.fail("密码错误");
        }

        // 生成token返回给前端
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        // 这里返回结果 可以单独设计一个接口
        return Result.success(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map()
        );

    }


    // 退出
    @GetMapping("/logout")
    @RequiresAuthentication // 权限校验
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }

}
