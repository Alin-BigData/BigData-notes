package com.wangfulin.vueblog.controller;


import com.wangfulin.vueblog.common.Result;
import com.wangfulin.vueblog.entity.User;
import com.wangfulin.vueblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import sun.tools.jconsole.inspector.XObject;

/**
 * <p>
 *  前端控制器
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

        return Result.success("200","ok",user);
    }
}
