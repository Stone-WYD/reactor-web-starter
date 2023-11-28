package com.wyd.reactorweb.config.property.core;

import lombok.Data;

/**
 * @program: reactor-web-starter
 * @description: 工作线程配置
 * @author: Stone
 * @create: 2023-11-28 14:31
 **/
@Data
public class WorkerProperties {

    private Integer corePoolSize;

    private Integer maximumPoolSize;

    private String threadNamePrefix;

}
