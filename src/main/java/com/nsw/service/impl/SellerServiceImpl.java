package com.nsw.service.impl;

import com.nsw.domain.SellerInfo;
import com.nsw.repository.SellerInfoRepository;
import com.nsw.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author nsw
 * @date 2018/9/27 15:53
 */
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }
}
