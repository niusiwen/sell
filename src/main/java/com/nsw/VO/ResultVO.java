package com.nsw.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * HTTP请求返回的最外层对象
 * @author nsw
 * @date 2018/9/17 21:35
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)  //这里注释掉使用全局的注解配置
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1653027102296272652L;

    /** 错误码 */
    private Integer code;

    /** 提示信息 */
    private String msg;

    /** 具体内容 */
    private T data;
}
