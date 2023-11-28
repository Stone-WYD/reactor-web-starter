package com.wyd.reactorweb.config.property;

import com.wyd.reactorweb.config.property.core.ResultStorage;
import com.wyd.reactorweb.config.property.core.WorkerProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: reactor-web-starter
 * @description: myReact 项目中关于反应式编程的一些核心配置
 * @author: Stone
 * @create: 2023-11-28 09:49
 **/
@Data
@ConfigurationProperties(prefix = "myreact.core")
public class CoreProperties {

    // 结果存放
    private ResultStorage resultStorage;

    // 线程配置
    private WorkerProperties appWorker;

    private WorkerProperties netWorker;

}

