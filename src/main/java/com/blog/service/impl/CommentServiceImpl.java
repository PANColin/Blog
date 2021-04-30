package com.blog.service.impl;

import com.blog.dao.CommentDao;
import com.blog.entity.Comment;
import com.blog.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("commentService")
public class CommentServiceImpl
        implements CommentService {
    @Resource
    private CommentDao commentDao;

    @Override
    public int add(Comment comment) {
        return this.commentDao.add(comment);
    }

    @Override
    public List<Comment> list(Map<String, Object> map) {
        return this.commentDao.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return this.commentDao.getTotal(map);
    }

    @Override
    public Integer delete(Integer id) {
        return this.commentDao.delete(id);
    }

    @Override
    public int update(Comment comment) {
        return this.commentDao.update(comment);
    }
}