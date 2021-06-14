# elasticsearch-demo
springboot集成es demo

***es版本 7.13.1***

1. 添加依赖,注意版本要修改成和使用版本一致

![自定义es版本](https://gitee.com/superjishere/images/raw/master/img/20210614141635.png)
![es依赖](https://gitee.com/superjishere/images/raw/master/img/20210614150535.png)

2. 创建配置类 

`com.superj.config.ElasticSearchClientConfig`

3. api测试
    
    - 索引相关
    - 文档相关
    
4. 项目demo

    环境:添加thymeleaf依赖.准备静态页面
    数据:jsoup包 - 解析网页 (tika - 解析视频)
    
    页面访问地址:http://localhost:9091/