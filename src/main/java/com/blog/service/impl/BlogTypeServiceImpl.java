package com.blog.service.impl;

import com.blog.dao.BlogTypeDao;
import com.blog.entity.BlogType;
import com.blog.service.BlogTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("blogTypeService")
public class BlogTypeServiceImpl
        implements BlogTypeService {
    @Resource
    private BlogTypeDao blogTypeDao;

    @Override
    public List<BlogType> countList() {
        return this.blogTypeDao.countList();
    }

    @Override
    public List<BlogType> list(Map<String, Object> map) {
        return this.blogTypeDao.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return this.blogTypeDao.getTotal(map);
    }

    @Override
    public Integer add(BlogType blogType) {
        return this.blogTypeDao.add(blogType);
    }

    @Override
    public Integer update(BlogType blogType) {
        return this.blogTypeDao.update(blogType);
    }

    @Override
    public Integer delete(Integer id) {
        return this.blogTypeDao.delete(id);
    }
}