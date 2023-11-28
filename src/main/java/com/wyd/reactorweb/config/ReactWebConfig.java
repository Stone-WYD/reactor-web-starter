package com.wyd.reactorweb.config;

import com.wyd.reactorweb.config.other.CoreConfig;
import com.wyd.reactorweb.config.other.MvcConfig;
import com.wyd.reactorweb.config.other.ServerRunConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @program: reactor_web
 * @description: 考虑到一些内容后期要封装成 spring-boot-starter 项目使用，此处使用一个配置类作为 starter 入口，并且在此引入其他配置类
 * @author: Stone
 * @create: 2023-11-13 18:19
 **/
@ConditionalOnProperty(name = "myreact.enable", havingValue = "true")
@Import({MvcConfig.class, ServerRunConfig.class, CoreConfig.class})
@Configuration
public class ReactWebConfig {


}
