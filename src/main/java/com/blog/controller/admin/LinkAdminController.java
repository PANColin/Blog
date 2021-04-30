package com.blog.controller.admin;


import com.blog.entity.Link;
import com.blog.entity.PageBean;
import com.blog.service.LinkService;
import com.blog.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping({"/admin/link"})
public class LinkAdminController {
    @Resource
    private LinkService linkService;

    @RequestMapping({"/list"})
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, HttpServletResponse response)
            throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap();
        map.put("start", Integer.valueOf(pageBean.getStart()));
        map.put("size", Integer.valueOf(pageBean.getPageSize()));
        List<Link> linkList = this.linkService.list(map);
        Long total = this.linkService.getTotal(map);
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(linkList);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping({"/save"})
    public String save(Link link, HttpServletResponse response)
            throws Exception {
        int resultTotal = 0;
        if (link.getId() == null) {
            resultTotal = this.linkService.add(link);
        } else {
            resultTotal = this.linkService.update(link);
        }
        JSONObject result = new JSONObject();
        if (resultTotal > 0) {
            result.put("success", Boolean.valueOf(true));
        } else {
            result.put("success", Boolean.valueOf(false));
        }
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping({"/delete"})
    public String delete(@RequestParam("ids") String ids, HttpServletResponse response)
            throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            this.linkService.delete(Integer.valueOf(Integer.parseInt(idsStr[i])));
        }
        JSONObject result = new JSONObject();
        result.put("success", Boolean.valueOf(true));
        ResponseUtil.write(response, result);
        return null;
    }
}