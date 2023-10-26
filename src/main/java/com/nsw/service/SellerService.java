package com.nsw.service;

import com.nsw.domain.SellerInfo;

/**
 * @author nsw
 * @date 2018/9/27 15:51
 */
public interface SellerService {

    /**
     * 通过openid查询卖家信息
     * @param openid
     * @return
     */
    SellerInfo findSellerInfoByOpenid(String openid);
}
