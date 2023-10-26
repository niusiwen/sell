package com.nsw.controller;

import com.nsw.constant.CookieConstant;
import com.nsw.constant.RedisConstant;
import com.nsw.domain.SellerInfo;
import com.nsw.enums.ResultEnum;
import com.nsw.service.SellerService;
import com.nsw.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 卖家用户
 * @author nsw
 * @date 2018/9/27 16:09
 */
@Controller
@RequestMapping("/seller")
public class SellerUserController {

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 扫码登陆
     * @param openid
     * @param response
     * @param map
     * @return
     */
    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                              HttpServletResponse response,
                              Map<String, Object> map) {
        //1.查询用户
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if(sellerInfo == null) {
            map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error");
        }

        //2.设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;//过期时间

        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), openid, expire, TimeUnit.SECONDS);

        //3.设置token至cookie
        /*Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(7200);//过期时间
        response.addCookie(cookie);*/
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire);
        //登陆成功跳转到订单列表页
        return new ModelAndView("redirect:"+"http://localhost:8080"+"/sell/seller/order/list");//建议使用绝对地址
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,HttpServletResponse response,
                       Map<String, Object> map) {

        //1.从cookie中查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie != null) {
            //2.清除redis
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
            //3.清楚cookie
            CookieUtil.set(response, CookieConstant.TOKEN, null, 0);

        }

        map.put("msg", ResultEnum.LOGOUT_SUCCESS);
        map.put("url", "/sell/seller/order/list");
        return new ModelAndView("common/success", map);
    }
}
