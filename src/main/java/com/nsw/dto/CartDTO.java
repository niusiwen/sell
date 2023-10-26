package com.nsw.dto;

import lombok.Data;

/**
 * 购物车
 * @author nsw
 * @date 2018/9/18 17:31
 */
@Data
public class CartDTO {

    /** 商品id. */
    private String productId;

    /** 数量. */
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
