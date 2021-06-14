package com.superj;

import com.alibaba.fastjson.JSON;
import com.superj.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SuperjEsApiApplicationTests {

    @Resource
    RestHighLevelClient client;
    /**====================================索引相关api===========================================*/
    // 测试创建索引  CreateIndexRequest
    @Test
    void testCreateIndex() throws IOException {
        //1.创建索引请求(索引名字不能有大写字母)
        CreateIndexRequest request = new CreateIndexRequest("superj_index");
        //2.客户端执行请求IndicesClient,请求后获得响应
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

        System.out.println(response);
    }

    //测试获取索引,只能判断存不存在
    @Test
    void testExistIndex() throws IOException {
        //1.获取索引请求
        GetIndexRequest request = new GetIndexRequest("superj_index");
        //2.客户端执行请求IndicesClient,请求后获得响应
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);

        System.out.println(exists);
    }

    //测试删除索引
    @Test
    void testDeleteIndex() throws IOException {
        //1.删除索引请求
        DeleteIndexRequest request = new DeleteIndexRequest("superj_index");
        //2.客户端执行请求IndicesClient,请求后获得响应
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);

        System.out.println(delete.isAcknowledged());//是否删除成功
    }
    /**====================================文档相关api===========================================*/
    //测试添加文档
    @Test
    void testAddDocument() throws IOException {
        //创建对象
        User user = new User("小姜是我", 18);
        //创建请求
        IndexRequest request = new IndexRequest("superj_index");//往哪个索引添加
        //添加请求规则
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
//        request.timeout("1s"); //同上
        //将数据放入请求  json格式 要引入fastjson依赖
        request.source(JSON.toJSONString(user), XContentType.JSON);
        //客户端发送请求
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

        System.out.println(response.toString());
        System.out.println(response.status());//状态 对应命令返回的状态 CREATED UPDATE
    }

    //测试查询文档是否存在
    @Test
    void testExistDocument() throws IOException {
        //创建请求
        GetRequest request = new GetRequest("superj_index","1");
        //不获取返回的_source的上下文了
        request.fetchSourceContext(new FetchSourceContext(false));
        //排序的字段
        request.storedFields("_none_");

        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //测试获取文档
    @Test
    void testGetDocument() throws IOException {
        //创建请求
        GetRequest request = new GetRequest("superj_index","1");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);

        System.out.println(response.getSourceAsString());
        System.out.println(response);//返回的全部内容和使用命令的方式是一样的
    }

    //测试更新文档
    @Test
    void testUpdateDocument() throws IOException {
        //创建请求
        UpdateRequest request = new UpdateRequest("superj_index","1");
        request.timeout("1s");

        User user = new User("小姜是我我是小姜", 3);
        request.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println(response.status());//是否更新成功
    }

    //测试更新文档
    @Test
    void testDeleteDocument() throws IOException {
        //创建请求
        DeleteRequest request = new DeleteRequest("superj_index","1");
        request.timeout("1s");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());//是否删除成功
    }

    //测试批量插入文档(批量更新/删除同理,只是请求不同)
    @Test
    void testBulk() throws IOException {
        BulkRequest request = new BulkRequest();
        request.timeout("10s");

        ArrayList<User> users = new ArrayList<>();
        users.add(new User("haha", 18));
        users.add(new User("BB", 1));
        users.add(new User("CC", 12));
        users.add(new User("DD", 5));
        users.add(new User("##", 28));
        users.add(new User("$$", 68));

        //批处理请求
        for (int i = 0; i < users.size(); i++) {
            request.add(new IndexRequest("superj_index")
                    .id("" + (i + 1)) //不写id 就默认生成随机id
                    .source(JSON.toJSONString(users.get(i)), XContentType.JSON));
        }

        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println(response.status());//是否成功
        System.out.println(response.hasFailures());//是否失败
    }

    //测试查询

    /**
     * 搜索请求 SearchRequest
     * 查询条件构造 SearchSourceBuilder
     * 高亮构造 HighlightBuilder
     * 精确查询 TermQueryBuilder
     * 匹配所有 MatchAllQueryBuilder
     * .......
     */
    @Test
    void testSearchRequest() throws IOException {
        SearchRequest request = new SearchRequest("superj_index");
        //构建搜索条件 - 查询构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询条件
        //精确匹配
        TermQueryBuilder termQuery = QueryBuilders.termQuery("name", "haha");
        //匹配所有
//        MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        //将查询条件放入查询构建器
        sourceBuilder.query(termQuery);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //放入请求
        request.source(sourceBuilder);
        //请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(response.getHits()));
        System.out.println("=============================");
        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }


    }
}
