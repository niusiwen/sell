package com.nsw.service;

/**
 * @author nsw
 * @date 2018/12/4 21:54
 */
public interface SecKillService {

    String querySecKillProductInfo(String productId);

    void orderProductMockDiffUser(String productId);
}
