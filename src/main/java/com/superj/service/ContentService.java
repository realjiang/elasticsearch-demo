package com.superj.service;

import com.alibaba.fastjson.JSON;
import com.superj.pojo.Content;
import com.superj.utils.HttpParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ContentService {
    @Resource
    private RestHighLevelClient client;

    /**
     * 解析数据,存入es
     * @param keyword 关键词
     * @return 是否成功
     * @throws Exception
     */
    public Boolean parseContent(String keyword) throws Exception {
        /**解析数据*/
        List<Content> contents = new HttpParseUtil().parseJD(keyword);
        /**放入es中(批量插入)*/
        BulkRequest request = new BulkRequest();
        request.timeout("2m");
        //批处理请求
        for (int i = 0; i < contents.size(); i++) {
            request.add(new IndexRequest("jd_goods")
                    .source(JSON.toJSONString(contents.get(i)), XContentType.JSON));
        }
        BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }
    /**
     * 获取数据,实现搜索功能
     * @param keyword 关键词
     * @param pageNum 从第几条开始
     * @param pageSize 查询几条
     * @return 是否成功
     * @throws Exception
     */
    public List<Map<String,Object>> searchPage(String keyword,int pageNum,int pageSize) throws Exception {
        if (pageNum <= 1) {
            pageNum = 1;
        }
        //条件搜索
        SearchRequest request = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //分页
        sourceBuilder.from(pageNum);
        sourceBuilder.size(pageSize);
        //精确匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        sourceBuilder.query(termQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //执行搜索
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //解析结果
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit documentFields : response.getHits().getHits()) {
            list.add(documentFields.getSourceAsMap());
        }
        return list;
    }
}
