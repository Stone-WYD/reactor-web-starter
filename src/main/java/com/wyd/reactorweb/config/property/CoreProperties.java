package com.wyd.reactorweb.config.property;

import com.wyd.reactorweb.config.property.core.ResultStorageProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @program: reactor-web-starter
 * @description: myReact 项目中关于反应式编程的一些核心配置
 * @author: Stone
 * @create: 2023-11-28 09:49
 **/
@Data
@ConfigurationProperties(prefix = "myreact.core")
@EnableConfigurationProperties({ResultStorageProperties.class})
public class CoreProperties {

    private ResultStorageProperties resultStorage;

    // 线程配置
    private WorkerProperties appWorker;

    private WorkerProperties netWorker;

    public static class WorkerProperties {

        private Integer corePoolSize;

        private Integer maximumPoolSize;

        private String threadNamePrefix;

        private Integer keepLiveMinutes;

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

        public Integer getKeepLiveMinutes() {
            return keepLiveMinutes;
        }

        public void setKeepLiveMinutes(Integer keepLiveMinutes) {
            this.keepLiveMinutes = keepLiveMinutes;
        }
    }

}

