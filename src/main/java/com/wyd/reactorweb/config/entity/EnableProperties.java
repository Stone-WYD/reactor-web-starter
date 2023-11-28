package com.wyd.reactorweb.config.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: reactor_web
 * @description: 启动配置类
 * @author: Stone
 * @create: 2023-11-20 10:57
 **/
@Data
@ConfigurationProperties(prefix = "myreact")
public class EnableProperties {
    private boolean enable;
}
