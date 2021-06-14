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

    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keyword) throws Exception {
        return contentService.parseContent(keyword);
    }
    @GetMapping("/search/{keyword}/{pageNum}/{pageSize}")
    public List<Map<String,Object>> search(@PathVariable("keyword") String keyword, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize) throws Exception {
        return contentService.searchPage(keyword, pageNum, pageSize);
    }
}
