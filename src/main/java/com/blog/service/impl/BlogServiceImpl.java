package com.blog.service.impl;

import com.blog.dao.BlogDao;
import com.blog.entity.Blog;
import com.blog.service.BlogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("blogService")
public class BlogServiceImpl
        implements BlogService {
    @Resource
    private BlogDao blogDao;

    @Override
    public List<Blog> countList() {
        return this.blogDao.countList();
    }

    @Override
    public List<Blog> list(Map<String, Object> map) {
        return this.blogDao.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return this.blogDao.getTotal(map);
    }

    @Override
    public Blog findById(Integer id) {
        return this.blogDao.findById(id);
    }

    @Override
    public Integer update(Blog blog) {
        return this.blogDao.update(blog);
    }

    @Override
    public Blog getLastBlog(Integer id) {
        return this.blogDao.getLastBlog(id);
    }

    @Override
    public Blog getNextBlog(Integer id) {
        return this.blogDao.getNextBlog(id);
    }

    @Override
    public Integer add(Blog blog) {
        return this.blogDao.add(blog);
    }

    @Override
    public Integer delete(Integer id) {
        return this.blogDao.delete(id);
    }

    @Override
    public Integer getBlogByTypeId(Integer typeId) {
        return this.blogDao.getBlogByTypeId(typeId);
    }
}