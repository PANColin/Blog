package com.blog.controller;


import com.blog.entity.Blog;
import com.blog.entity.PageBean;
import com.blog.service.BlogService;
import com.blog.util.PageUtil;
import com.blog.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping({"/"})
public class IndexController {
    @Resource
    private BlogService blogService;

    @RequestMapping({"/index"})
    public ModelAndView index(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "typeId", required = false) String typeId, @RequestParam(value = "releaseDateStr", required = false) String releaseDateStr, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        if (StringUtil.isEmpty(page)) {
            page = "1";
        }
        PageBean pageBean = new PageBean(Integer.parseInt(page), 10);
        Map<String, Object> map = new HashMap();
        map.put("start", Integer.valueOf(pageBean.getStart()));
        map.put("size", Integer.valueOf(pageBean.getPageSize()));
        map.put("typeId", typeId);
        map.put("releaseDateStr", releaseDateStr);
        List<Blog> blogList = this.blogService.list(map);
        for (Blog blog : blogList) {
            List<String> imagesList = blog.getImagesList();
            String blogInfo = blog.getContent();
            Document doc = Jsoup.parse(blogInfo);
            //获取 带有src属性的img元素
            Elements jpgs = doc.select("img[src$=.jpg]");
            for (int i = 0; i < jpgs.size(); i++) {
                Element jpg = jpgs.get(i);
                imagesList.add(jpg.toString());
                //设置首页list.jsp显示图片的个数
                if (i == 2) {
                    break;
                }
            }
        }
        mav.addObject("blogList", blogList);
        StringBuffer param = new StringBuffer();
        if (StringUtil.isNotEmpty(typeId)) {
            param.append("typeId=" + typeId + "&");
        }
        if (StringUtil.isNotEmpty(releaseDateStr)) {
            param.append("releaseDateStr=" + releaseDateStr + "&");
        }
        mav.addObject("pageCode", PageUtil.genPagination(request.getContextPath() + "/index.html", this.blogService.getTotal(map).longValue(), Integer.parseInt(page), 10, param.toString()));
        mav.addObject("mainPage", "foreground/blog/list.jsp");
        mav.addObject("pageTitle", "Java个人博客系统");
        mav.setViewName("mainTemp");
        return mav;
    }
}