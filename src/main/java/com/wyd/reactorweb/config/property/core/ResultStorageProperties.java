package com.wyd.reactorweb.config.property.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: reactor-web-starter
 * @description: 结果存储配置类
 * @author: Stone
 * @create: 2023-11-28 14:32
 **/
@Data
@ConfigurationProperties(prefix = "myreact.core.result-storage")
public class ResultStorageProperties {

    private String type;

    private LocalProperties localProperties;

    public static class LocalProperties {

        // 缓存初始大小
        private Integer initalCapacity;

        // 并发数量
        private Integer concurrencyLevel;

        // 写入多久后失效，单位分钟
        private Integer expireAfterWriteMinutes;

        public Integer getInitalCapacity() {
            return initalCapacity;
        }

        public void setInitalCapacity(Integer initalCapacity) {
            this.initalCapacity = initalCapacity;
        }

        public Integer getConcurrencyLevel() {
            return concurrencyLevel;
        }

        public void setConcurrencyLevel(Integer concurrencyLevel) {
            this.concurrencyLevel = concurrencyLevel;
        }

        public Integer getExpireAfterWriteMinutes() {
            return expireAfterWriteMinutes;
        }

        public void setExpireAfterWriteMinutes(Integer expireAfterWriteMinutes) {
            this.expireAfterWriteMinutes = expireAfterWriteMinutes;
        }
    }


}
