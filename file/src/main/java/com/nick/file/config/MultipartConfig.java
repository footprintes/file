package com.nick.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.MultipartConfigElement;

/**
 * @version V1.0
 * @ClassName：MultipartConfig 文件上传配置
 * @author: hbj
 * @CreateDate：2018/8/21 17:47
 */
@Configuration
@PropertySource(value = "classpath:application.yml")
public class MultipartConfig {
    @Bean
    public MultipartConfigElement multipartConfigElement(
            @Value("${spring.servlet.multipart.max-request-size}") String maxRequestSize,
            @Value("${spring.servlet.multipart.max-file-size}") String maxFileSize) {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件最大
        factory.setMaxFileSize(maxFileSize);
        // 设置总上传数据总大小
        factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }
}
