package com.wyd.reactorweb.config.property;

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

    private String test;

    public static class ResultStorage {

    }

    public static class WorkerProperties {
        private Integer corePoolSize;

        private Integer maximumPoolSize;

        private String threadNamePrefix;

        public Integer getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(Integer corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public Integer getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(Integer maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public String getThreadNamePrefix() {
            return threadNamePrefix;
        }

        public void setThreadNamePrefix(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }
    }
}

