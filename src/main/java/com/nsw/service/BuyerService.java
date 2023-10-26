package com.nsw.service;

import com.nsw.dto.OrderDTO;

/**
 * 买家
 * @author nsw
 * @date 2018/9/19 20:46
 */
public interface BuyerService {

    //查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderId);

}
