package com.blog.controller.admin;


import com.blog.entity.Comment;
import com.blog.entity.PageBean;
import com.blog.service.CommentService;
import com.blog.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping({"/admin/comment"})
public class CommentAdminController {
    @Resource
    private CommentService commentService;

    @RequestMapping({"/list"})
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, @RequestParam(value = "state", required = false) String state, HttpServletResponse response)
            throws Exception {
        PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
        Map<String, Object> map = new HashMap();
        map.put("start", Integer.valueOf(pageBean.getStart()));
        map.put("size", Integer.valueOf(pageBean.getPageSize()));
        map.put("state", state);
        List<Comment> commentList = this.commentService.list(map);
        Long total = this.commentService.getTotal(map);
        JSONObject result = new JSONObject();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
        JSONArray jsonArray = JSONArray.fromObject(commentList, jsonConfig);
        result.put("rows", jsonArray);
        result.put("total", total);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping({"/delete"})
    public String delete(@RequestParam("ids") String ids, HttpServletResponse response)
            throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            this.commentService.delete(Integer.valueOf(Integer.parseInt(idsStr[i])));
        }
        JSONObject result = new JSONObject();
        result.put("success", Boolean.valueOf(true));
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping({"/review"})
    public String review(@RequestParam("ids") String ids, @RequestParam("state") Integer state, HttpServletResponse response)
            throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            Comment comment = new Comment();
            comment.setState(state);
            comment.setId(Integer.valueOf(Integer.parseInt(idsStr[i])));
            this.commentService.update(comment);
        }
        JSONObject result = new JSONObject();
        result.put("success", Boolean.valueOf(true));
        ResponseUtil.write(response, result);
        return null;
    }
}