package com.nsw.exception;

import com.nsw.enums.ResultEnum;
import lombok.Getter;

/**
 * 自定义商品异常
 * @author nsw
 * @date 2018/9/18 16:34
 */
@Getter
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
