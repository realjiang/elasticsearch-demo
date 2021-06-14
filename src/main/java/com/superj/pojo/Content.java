package com.superj.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Content {

    private String img;
    private String price;
    private String title;
    //可以自己扩展其他属性
}
