package com.nsw.service.impl;

import com.nsw.converter.OrderMaster2OrderDTOConverter;
import com.nsw.domain.OrderDetail;
import com.nsw.domain.OrderMaster;
import com.nsw.domain.ProductInfo;
import com.nsw.dto.CartDTO;
import com.nsw.dto.OrderDTO;
import com.nsw.enums.OrderStatusEnum;
import com.nsw.enums.PayStatusEnum;
import com.nsw.enums.ResultEnum;
import com.nsw.exception.SellException;
import com.nsw.repository.OrderDetailRepository;
import com.nsw.repository.OrderMasterRepository;
import com.nsw.service.OrderService;
import com.nsw.service.PayService;
import com.nsw.service.ProductService;
import com.nsw.service.WebSocket;
import com.nsw.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author nsw
 * @date 2018/9/18 16:20
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private PayService payService;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.getUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

//        List<CartDTO> cartDTOList = new ArrayList<>();

        //1.查询商品（数量，价格）
        //lambda 表达式只能引用标记了 final 的外层局部变量，这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误。
        /*orderDTO.getOrderDetailList().forEach(orderDetail -> {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if(productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2.计算订单的总价
            orderAmount = orderDetail.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
            .add(orderAmount);
        });*/
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if(productInfo == null){//由于新版本的jpa findOne方法查询不到是直接抛异常，所以这一步查询的时候就做好了，可以省略
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2.计算订单的总价  商品的价格后台获取，数量前台传过来
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            //订单详情入库
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail);//这里将商品表的相关属性拷贝到订单明细表
            orderDetailRepository.save(orderDetail);

//            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(), orderDetail.getProductQuantity());
//            cartDTOList.add(cartDTO);
        }

        //3.写入订单数据库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);//这里先拷贝 再设置属性值 防止值被null值覆盖掉
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());//这里重设订单状态和支付状态的默认值
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());//
        orderMasterRepository.save(orderMaster);

        //4.扣库存（orderMaster和orderDetail）
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> {
                    return new CartDTO(e.getProductId(), e.getProductQuantity());
                }).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        //发送websocket消息
        webSocket.sendMessage(orderDTO.getOrderId());

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {

        //Optional对象中有值存在则返回此值，如果没有值存在，则会抛出NoSuchElementException异常
        //OrderMaster orderMaster = orderMasterRepository.findById(orderId).get();
        /*if(orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }*/

        //Optional新的写法
        Optional<OrderMaster> optionalOrderMaster = orderMasterRepository.findById(orderId);
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(optionalOrderMaster.orElseThrow(() -> new SellException(ResultEnum.ORDER_NOT_EXIST)), orderDTO);

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        /*OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);*/
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);

        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        //判断订单的状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){//不是新下单，不能取消
            log.error("【取消订单】 订单的状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());//这里修改orderDTO的OrderStatus
        BeanUtils.copyProperties(orderDTO, orderMaster);//修改完OrderStatus，再拷贝到orderMaster，这样返回的数据才正确
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);//更新
        if(updateResult == null){
            log.error("【取消订单】 更新失败, orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //返还库存
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【取消订单】 订单中无商品详情, orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        //
        productService.increaseStock(cartDTOList);
        //如果已支付，需要退款
        if(orderDTO.getOrderStatus().equals(PayStatusEnum.SUCCESS)){
            payService.refund(orderDTO);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单】 订单的状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISH.getCode());//这里修改orderDTO的OrderStatus为完成状态
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);//修改完OrderStatus，再拷贝到orderMaster，这样返回的数据才正确
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);//更新
        if(updateResult == null){
            log.error("【完结订单】 更新失败, orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【支付订单】 订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【支付订单】 订单支付状态不正确，orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());//修改支付状态
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);//修改完payStatus，再拷贝到orderMaster，这样返回的数据才正确
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);//更新
        if(updateResult == null){
            log.error("【支付订单】 更新失败, orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }
}
