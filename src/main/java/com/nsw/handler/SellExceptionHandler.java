package com.nsw.handler;

import com.nsw.VO.ResultVO;
import com.nsw.exception.ResponseBankException;
import com.nsw.exception.SellException;
import com.nsw.exception.SellerAuthorizeExcetion;
import com.nsw.utils.ResultVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常处理类
 * @author nsw
 * @date 2018/10/9 20:27
 */
@ControllerAdvice
public class SellExceptionHandler {

    @Autowired
    //private ProjectUrlConfig projectUrlConfig

    //拦截登陆异常
    @ExceptionHandler(value = SellerAuthorizeExcetion.class)
    public ModelAndView handlerAuthorizeException() {
        //登陆异常跳转到登陆页面
//        return new ModelAndView("redirect:"
//                .concat());


        return null;
    }

    //商品不存在异常处理
    @ExceptionHandler(value = SellException.class)
    @ResponseBody
    public ResultVO handlerSellerException(SellException e) {
        return ResultVoUtil.error(e.getCode(), e.getMessage());
    }

    //测试-->更改返回状态值
    @ExceptionHandler(value = ResponseBankException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handlerResponseBankException(SellException e) {

    }
}
