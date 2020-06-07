package com.wangfulin.vueblog.service.impl;

import com.wangfulin.vueblog.entity.Blog;
import com.wangfulin.vueblog.mapper.BlogMapper;
import com.wangfulin.vueblog.service.BlogService;
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
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
