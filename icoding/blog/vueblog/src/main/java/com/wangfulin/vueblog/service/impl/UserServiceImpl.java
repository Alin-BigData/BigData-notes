package com.wangfulin.vueblog.service.impl;

import com.wangfulin.vueblog.entity.User;
import com.wangfulin.vueblog.mapper.UserMapper;
import com.wangfulin.vueblog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2020-06-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
