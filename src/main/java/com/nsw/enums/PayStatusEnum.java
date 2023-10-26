package com.nsw.enums;

import lombok.Getter;

/**
 * @author nsw
 * @date 2018/9/18 15:23
 */
//@Getter
public enum PayStatusEnum implements CodeEnum {

    WAIT(0, "等待支付"),
    SUCCESS(1, "支付成功"),
    ;

    private Integer code;

    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    //为了更明显的看到实现接口中的方法，这里不用@Getter，直接写get()方法
    @Override
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
