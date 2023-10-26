package com.nsw.enums;

import lombok.Getter;

/**
 * @author nsw
 * @date 2018/9/18 15:19
 */
@Getter
public enum OrderStatusEnum implements CodeEnum {
    NEW(0, "新订单"),
    FINISH(1, "完成"),
    CANCEL(2, "已取消"),
    ;

    private Integer code;

    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
