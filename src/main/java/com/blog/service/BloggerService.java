package com.blog.service;

import com.blog.entity.Blogger;

public interface BloggerService
{
  public abstract Blogger find();
  
  public abstract Blogger getByUserName(String paramString);
  
  public abstract Integer update(Blogger paramBlogger);
}