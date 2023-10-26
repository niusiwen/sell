package com.nsw.controller;

import com.lly835.bestpay.model.PayResponse;
import com.nsw.dto.OrderDTO;
import com.nsw.enums.ResultEnum;
import com.nsw.exception.SellException;
import com.nsw.service.OrderService;
import com.nsw.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 支付
 * @author nsw
 * @date 2019/4/6 18:08
 */
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    /**
     * 创建支付订单-发起支付
     * @param orderId
     * @param returnUrl
     * @param map
     * @return
     */
    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String,Object> map){

        //1.查询订单
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //2 发起支付
        PayResponse payResponse = payService.create(orderDTO);

        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);

        return new ModelAndView("pay/create", map);
    }

    /**
     * 微信异步通知
     */
    @PostMapping("/notify")
    public ModelAndView wechatNotify(@RequestBody String notifyData){

        payService.notify(notifyData);

        //返回给微信的处理结果，让微信不用再一直推支付的结果
        return new ModelAndView("pay/success");

    }
}
