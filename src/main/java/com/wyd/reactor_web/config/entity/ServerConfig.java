package com.wyd.reactor_web.config.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: reactor_web
 * @description: 服务属性类
 * @author: Stone
 * @create: 2023-11-17 18:22
 **/
@Data
@ConfigurationProperties(prefix = "mynetty.server")
public class ServerConfig {

    private Integer port;

}
