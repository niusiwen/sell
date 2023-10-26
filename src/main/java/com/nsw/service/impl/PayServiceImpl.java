package com.nsw.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.nsw.dto.OrderDTO;
import com.nsw.enums.ResultEnum;
import com.nsw.exception.SellException;
import com.nsw.service.OrderService;
import com.nsw.service.PayService;
import com.nsw.utils.JsonUtil;
import com.nsw.utils.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author nsw
 * @date 2019/4/6 18:16
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    private static final String ORDER_NAME = "微信点餐订单";

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信支付】发起支付 request={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("【微信支付】发起支付 payResponse={}", JsonUtil.toJson(payResponse));
        return payResponse;
    }

    @Override
    public PayResponse notify(String notifyData) {
        //1.验证签名
        //2.验证支付的状态
        //3.验证支付的金额
        //4.验证支付人（某些场景下：下单人==支付人）根据业务来定

        //1和2两点sdk已经做了，这里只需要做一下3
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付】 异步通知，payResponse={}", payResponse);

        //查询订单
        OrderDTO orderDTO = orderService.findOne(payResponse.getOrderId());

        //判断订单是否存在
        if(orderDTO == null){
            log.error("【微信支付】异步通知，订单不存在");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //判断金额是否一致(0.1  0.10 不一样)
        /*if(orderDTO.getOrderAmount().compareTo(new BigDecimal(payResponse.getOrderAmount()))!=0){
            log.error("");
        }*/
        //上面的金额校验方法因为精度的问题，会判断错误，使用自己定义的比较方法
        if(!MathUtil.equals(payResponse.getOrderAmount(),orderDTO.getOrderAmount().doubleValue())){
            log.error("【微信支付】异步通知, 订单金额不一致, orderId={}, 微信通知金额={}, 系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderDTO.getOrderAmount());
            throw new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

        //修改订单的支付状态
        orderService.paid(orderDTO);

        return payResponse;
    }

    /**
     * 退款
     * @param orderDto
     */
    @Override
    public RefundResponse refund(OrderDTO orderDto) {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderId(orderDto.getOrderId());
        refundRequest.setOrderAmount(orderDto.getOrderAmount().doubleValue());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【微信退款】 request={}",JsonUtil.toJson(refundRequest));
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        log.info("【微信退款】 response={}",JsonUtil.toJson(refundResponse));
        return refundResponse;
    }
}
