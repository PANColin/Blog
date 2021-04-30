package com.blog.entity;

import java.io.Serializable;

public class PageBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int page;
    private int pageSize;
    private int start;

    public PageBean(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return (this.page - 1) * this.pageSize;
    }
}



