package com.wyd.reactorweb.test.mvc.entity;

import lombok.Data;

/**
 * @program: reactor_web
 * @description: 测试类
 * @author: Stone
 * @create: 2023-11-17 16:53
 **/
@Data
public class Names {

    private String name;

    private String name2;

    private Integer name3;

    private String name4;

    @Override
    public String toString() {
        return "Names{" +
                "name='" + name + '\'' +
                ", name2='" + name2 + '\'' +
                ", name3=" + name3 +
                ", name4='" + name4 + '\'' +
                '}';
    }
}
