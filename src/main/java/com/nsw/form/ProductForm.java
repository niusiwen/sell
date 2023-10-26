package com.nsw.form;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author nsw
 * @date 2018/9/27 10:13
 */
@Data
public class ProductForm {

    private String productId;

    /** 名字. */
    private String productName;

    /** 单价. */
    private BigDecimal productPrice;

    /** 库存. */
    private Integer productStock;

    /** 描述. */
    private String productDescription;

    /** 小图. */
    private String productIcon;

    /** 状态, 0正常1下架. */
    //private Integer productStatus;// = ProductStatusEnum.UP.getCode();

    /** 类目编号. */
    private Integer categoryType;
}
