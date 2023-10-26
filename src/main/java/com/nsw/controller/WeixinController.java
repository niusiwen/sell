package com.nsw.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 用来测试微信获取openid流程的测试controller
 * @author nsw
 * @date 2020/12/22 14:01
 */
@RequestMapping("/weixin")
@Slf4j
@RestController
public class WeixinController {


    /**
     * 获取code后，使用code获取openId的方法
     * @param code
     */
    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code) {
        log.info("进入auth方法。。。");
        log.info("code={}", code);

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxd898fcb01713c658&secret=29d8a650db31472aa87800e3b0d739f2&code=" + code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("response={}", response);
    }

}
