package com.wyd.reactor_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: spring-wyd
 * @description: spring_netty_test spring启动类
 * @author: Stone
 * @create: 2023-10-18 13:58
 **/
@SpringBootApplication
public class MySpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringApplication.class, args);
        System.out.println("启动了 netty 服务器~");
    }
}
