package com.hpd.butler.config;

import com.hpd.butler.constant.GlobalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * 初始化静态配置
 */
@Configuration
public class GlobalConfigInitializer {

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        GlobalConfig.setAdminPath(env.getProperty("adminPath"));
        GlobalConfig.setRestApiPath(env.getProperty("restPath"));
    }
}
