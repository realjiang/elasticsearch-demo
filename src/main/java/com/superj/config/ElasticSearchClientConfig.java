package com.superj.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //相当于spring中的ApplicationContext.xml
public class ElasticSearchClientConfig {

    @Bean // 方法名-spring的id 返回值-spring的class
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder( //注册es节点,如果是集群就注册多个
                new HttpHost("127.0.0.1", 9200, "http")));
        return client;
    }
}
