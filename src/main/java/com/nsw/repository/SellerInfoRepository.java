package com.nsw.repository;

import com.nsw.domain.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author nsw
 * @date 2018/9/27 15:43
 */
public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {

    SellerInfo findByOpenid(String openid);
}
