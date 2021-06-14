package com.superj.utils;

import com.superj.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class HttpParseUtil {
//    public static void main(String[] args) throws IOException {
//        new HttpParseUtil().parseJD("码出高效").forEach(System.out::println);
//    }

    public List<Content> parseJD(String keywords) throws IOException {
        //前提:需要联网
        //获取不到ajax
        //获取请求 https://search.jd.com/Search?keyword=java&enc=utf-8   &enc=utf-8解决中文乱码
        String url = "https://search.jd.com/Search?keyword="+keywords+"&enc=utf-8";
        //解析网页(返回的就是浏览器的Document对象) - jsoup低版本可添加请求头绕过登录
//        Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 5.1; zh-CN) AppleWebKit/535.12 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/535.12").timeout(30000).get();
        Document document = Jsoup.parse(new URL(url), 30000);//jsoup版本1.13.1
        //js能用的方法这里都能用
        Element element = document.getElementById("J_goodsList");
        //获取所有li元素
        Elements lis = element.getElementsByTag("li");
        //数据结果集
        ArrayList<Content> goodList = new ArrayList<>();
        //获取元素中内容
        for (Element li : lis) {
            //数据
            Content good = new Content();
            //图片多的网站都是延迟加载的
            String img = li.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = li.getElementsByClass("p-price").eq(0).text();
            String title = li.getElementsByClass("p-name").eq(0).text();
            good.setImg(img);
            good.setPrice(price);
            good.setTitle(title);
            goodList.add(good);
        }
        return goodList;
    }
}
