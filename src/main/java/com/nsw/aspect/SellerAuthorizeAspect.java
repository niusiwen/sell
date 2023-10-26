package com.nsw.aspect;

import com.nsw.constant.CookieConstant;
import com.nsw.constant.RedisConstant;
import com.nsw.exception.SellerAuthorizeExcetion;
import com.nsw.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * aop 身份验证
 * @author nsw
 * @date 2018/9/27 17:00
 */
@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 切入点
     */
   @Pointcut("execution(public * com.nsw.controller.Seller*.*(..))" +
    "&& !execution(public * com.nsw.controller.SellerUserController.*(..))")
    public void verify() {

    }

    /**
     * 在切入点之前进行身份校验
     */
    @Before("verify()")
    public void doVerify() {
        //获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //查询cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie == null){
            log.warn("【登陆校验】 Cookie中查不到token");
            throw new SellerAuthorizeExcetion();
        }

        //去redis里查询
        String tokenValue = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
        if(StringUtils.isEmpty(tokenValue)){
            log.warn("【登陆校验】 Redis中查不到token");
            throw new SellerAuthorizeExcetion();
        }
    }
}
