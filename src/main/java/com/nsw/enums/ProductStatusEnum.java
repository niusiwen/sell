package com.nsw.enums;

import lombok.Getter;

/**
 * 商品状态
 * @author nsw
 * @date 2018/9/17 17:00
 */
@Getter //给每个属性提供get方法，代码中不用写了
public enum ProductStatusEnum implements CodeEnum {

    UP(0, "在售"),
    DOWN(1, "下架")
    ;

    private Integer code;

    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
