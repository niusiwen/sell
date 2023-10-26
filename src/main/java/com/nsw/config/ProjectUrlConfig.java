package com.nsw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * spring boot 2.x 中对配置文件中名称的命名规范进行了强制约束，需要使用kebab格式，
 * 不能使用java中的驼峰式写法了，分隔符使用 ’-‘ 来分隔
 *
 * 项目相关地址配置
 * @author nsw
 * @date 2020/12/22 15:22
 */
@Data
@ConfigurationProperties(prefix = "project-url")
@Component
public class ProjectUrlConfig {

    /**
     * 微信公众平台授权url
     */
    public String wechatMpAuthorize;

    /**
     * 微信开放平台授权url
     */
    public String wechatOpenAuthorize;

    /**
     * 点餐系统
     */
    public String sell;

}
