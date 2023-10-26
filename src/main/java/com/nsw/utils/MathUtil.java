package com.nsw.utils;

/**
 * @author nsw
 * @date 2019/4/6 20:32
 */
public class MathUtil {

    private static final Double MONEY_RANGE = 0.01;

    /**
     * 比较两个金额是否相等
     * @param d1
     * @param d2
     * @return
     */
    public static Boolean equals(Double d1, Double d2){
        Double result = Math.abs(d1 - d2);
        //相差小于0.01当做金额相等
        if(result < MONEY_RANGE){
            return true;
        }
        return false;
    }
}
