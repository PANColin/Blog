package com.blog.service.impl;

import com.blog.dao.BloggerDao;
import com.blog.entity.Blogger;
import com.blog.service.BloggerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("bloggerService")
public class BloggerServiceImpl
        implements BloggerService {
    @Resource
    private BloggerDao bloggerDao;

    @Override
    public Blogger find() {
        return this.bloggerDao.find();
    }

    @Override
    public Blogger getByUserName(String userName) {
        return this.bloggerDao.getByUserName(userName);
    }

    @Override
    public Integer update(Blogger blogger) {
        return this.bloggerDao.update(blogger);
    }
}