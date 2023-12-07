package com.wyd.reactorweb.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: reactor_web
 * @description: 服务属性类
 * @author: Stone
 * @create: 2023-11-17 18:22
 **/
@Data
@ConfigurationProperties(prefix = "myreact.server")
public class ServerProperties {

    private Integer port;

    private Integer bossLoopGroupThreadNum;

    private Integer workerLoopGroupThreadNum;

    private Integer businessThreadNum;

    public void setBusinessThreadNum(Integer businessThreadNum) {
        this.businessThreadNum = businessThreadNum;
    }
}
