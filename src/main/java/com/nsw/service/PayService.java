package com.nsw.service;

import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundResponse;
import com.nsw.dto.OrderDTO;

/**
 * 支付
 * @author nsw
 * @date 2019/4/6 18:16
 */
public interface PayService {

    PayResponse create(OrderDTO orderDTO);

    PayResponse notify(String notifyData);

    RefundResponse refund(OrderDTO orderDto);
}
