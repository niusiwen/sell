package com.nsw.service.impl;

import com.nsw.dto.OrderDTO;
import com.nsw.enums.ResultEnum;
import com.nsw.exception.SellException;
import com.nsw.service.BuyerService;
import com.nsw.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author nsw
 * @date 2018/9/19 20:57
 */
@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {

        return checkOrderOwner(openid, orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO = checkOrderOwner(openid, orderId);
        if(orderDTO == null) {
            log.error("【取消订单】 查不到该订单. orderId={}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        return orderService.cancel(orderDTO);
    }

    //判断订单是否属于当前用户 是->返回订单
    private OrderDTO checkOrderOwner(String openid, String orderId) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(orderDTO == null) {
            return null;
        }
        //判断订单是否属于当前用户
        if(orderDTO.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("【查询订单】 订单openid不一致. openid={},orderDTO={}",openid,orderDTO);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
