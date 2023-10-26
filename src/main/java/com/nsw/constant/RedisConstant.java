package com.nsw.constant;

/**
 * redis常量
 * @author nsw
 * @date 2018/9/27 16:32
 */
public interface RedisConstant {

    //key前缀
    String TOKEN_PREFIX = "token_%s";

    //过期时间
    Integer EXPIRE = 7200; //2小时
}
