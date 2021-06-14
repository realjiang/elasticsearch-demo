package com.superj;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class SuperjEsApiApplicationTests {

    @Resource
    RestHighLevelClient client;

    // 测试创建索引  CreateIndexRequest
    @Test
    void testCreateIndex() throws IOException {
        //1.创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("superj_index");
        //2.客户端执行请求IndicesClient,请求后获得响应
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

        System.out.println(response);
    }

    //测试获取索引,只能判断存不存在
    @Test
    void testExistIndex() throws IOException {
        //1.创建索引请求
        GetIndexRequest request = new GetIndexRequest("superj_index");
        //2.客户端执行请求IndicesClient,请求后获得响应
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);

        System.out.println(exists);
    }

    //测试删除索引
    @Test
    void testDeleteIndex() throws IOException {
        //1.创建索引请求
        DeleteIndexRequest request = new DeleteIndexRequest("superj_index");
        //2.客户端执行请求IndicesClient,请求后获得响应
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);

        System.out.println(delete.isAcknowledged());//是否删除成功
    }
}
