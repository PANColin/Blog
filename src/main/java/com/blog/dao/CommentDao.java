package com.blog.dao;

import com.blog.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentDao
{
  public abstract int add(Comment paramComment);
  
  public abstract int update(Comment paramComment);
  
  public abstract List<Comment> list(Map<String, Object> paramMap);
  
  public abstract Long getTotal(Map<String, Object> paramMap);
  
  public abstract Integer delete(Integer paramInteger);
}