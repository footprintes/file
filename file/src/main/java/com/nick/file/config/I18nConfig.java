package com.nick.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


/**
 * @version V1.0
 * @ClassName：I18nConfig
 * @author: hbj
 * @CreateDate：2018/8/21 13:56
 */
@Configuration
public class I18nConfig extends WebMvcConfigurationSupport {
    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocaleResolver();
    }
}
