package com.blog.service.impl;

import com.blog.dao.LinkDao;
import com.blog.entity.Link;
import com.blog.service.LinkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("linkService")
public class LinkServiceImpl
        implements LinkService {
    @Resource
    private LinkDao linkDao;

    @Override
    public int add(Link link) {
        return this.linkDao.add(link);
    }

    @Override
    public int update(Link link) {
        return this.linkDao.update(link);
    }

    @Override
    public List<Link> list(Map<String, Object> map) {
        return this.linkDao.list(map);
    }

    @Override
    public Long getTotal(Map<String, Object> map) {
        return this.linkDao.getTotal(map);
    }

    @Override
    public Integer delete(Integer id) {
        return this.linkDao.delete(id);
    }
}