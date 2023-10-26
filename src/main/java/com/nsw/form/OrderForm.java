package com.nsw.form;

import lombok.Data;
//import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotEmpty;

/**
 * 订单
 * 用来进行表单验证的数据对象
 * @author nsw
 * @date 2018/9/18 21:35
 */
@Data
public class OrderForm {

    /**
     * 买家姓名
     */
    @NotEmpty(message = "姓名必填")
    private String name;

    /**
     * 买家手机号
     */
    @NotEmpty(message = "姓名必填")
    private String phone;

    /**
     * 买家地址
     */
    @NotEmpty(message = "地址必填")
    private String address;

    /**
     * 买家微信openid
     */
    @NotEmpty(message = "openid必填")
    private String openid;

    /**
     * 购物车
     */
    @NotEmpty(message = "购物车不能为空")
    private String items;

}
