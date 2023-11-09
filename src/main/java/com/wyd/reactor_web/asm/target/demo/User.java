package com.wyd.reactor_web.asm.target.demo;

import lombok.Data;
import lombok.ToString;

/**
 * @program: reactor_web
 * @description: 用户类
 * @author: Stone
 * @create: 2023-11-09 10:24
 **/
@Data
public class User {

    private String name;

    private String hobby;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}
