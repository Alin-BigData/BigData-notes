package com.wangfulin.vueblog.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangfulin.vueblog.common.Result;
import com.wangfulin.vueblog.dto.LoginDto;
import com.wangfulin.vueblog.entity.User;
import com.wangfulin.vueblog.service.UserService;
import com.wangfulin.vueblog.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import sun.tools.jconsole.inspector.XObject;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author
 * @since 2020-06-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/index")
    public Result inde() {
        User user = userService.getById(1L);

        return Result.success(200, "ok", user);
    }


}
