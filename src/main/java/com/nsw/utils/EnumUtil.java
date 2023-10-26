package com.nsw.utils;

import com.nsw.enums.CodeEnum;

/**
 * @author nsw
 * @date 2018/9/26 9:33
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumCLass) {
        for(T each: enumCLass.getEnumConstants()) {
            if(code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
