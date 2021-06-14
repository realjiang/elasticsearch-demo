package com.superj.controller;

import com.superj.service.ContentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class ContentController {

    @Resource
    private ContentService contentService;
    //解析数据存入es
    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keyword) throws Exception {
        return contentService.parseContent(keyword);
    }
    //基础搜索接口
    @GetMapping("/search/{keyword}/{pageNum}/{pageSize}")
    public List<Map<String,Object>> search(@PathVariable("keyword") String keyword, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) throws Exception {
        return contentService.searchPage(keyword, pageNum, pageSize);
    }
    //高亮版搜索接口
    @GetMapping("/search_hl/{keyword}/{pageNum}/{pageSize}")
    public List<Map<String,Object>> search_hl(@PathVariable("keyword") String keyword, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) throws Exception {
        return contentService.searchPageHighLight(keyword, pageNum, pageSize);
    }
}
