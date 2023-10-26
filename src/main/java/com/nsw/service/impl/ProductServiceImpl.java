package com.nsw.service.impl;

import com.nsw.domain.ProductInfo;
import com.nsw.dto.CartDTO;
import com.nsw.enums.ProductStatusEnum;
import com.nsw.enums.ResultEnum;
import com.nsw.exception.SellException;
import com.nsw.repository.ProductInfoRepository;
import com.nsw.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author nsw
 * @date 2018/9/17 16:51
 */
@Service
@CacheConfig(cacheNames = "product") //统一配置cacheNames，下面的注解就可以省略不写
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository repository;

    @Override
    @Cacheable(key = "123") //缓存注解，将查询的结果放入缓存中
    public ProductInfo findOne(String productId) {
        //return repository.findById(productId).get(); //这里不直接get(),使用orElseThrow()对异常进行处理
        Optional<ProductInfo> productInfoOptional = repository.findById(productId);
        return productInfoOptional.orElseThrow(()->new SellException(ResultEnum.PRODUCT_NOT_EXIST));
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @CachePut(key = "123")  //当修改内容之后，将修改后的内容更新到redis缓存，但返回值要与查询缓存的保持一致
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).get();
            if(productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).get();
            if(productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if(result < 0){
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }

    @Override
    public ProductInfo onSale(String productId) {
        Optional<ProductInfo> productInfoOptional = repository.findById(productId);
        //
        if(productInfoOptional.orElseThrow(() -> new SellException(ResultEnum.PRODUCT_NOT_EXIST)).getProductStatusEnum().equals(ProductStatusEnum.UP)){
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //更新
        ProductInfo productInfo = productInfoOptional.get();
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return repository.save(productInfo);
    }

    @Override
    public ProductInfo offSale(String productId) {
        Optional<ProductInfo> productInfoOptional = repository.findById(productId);
        //
        if(productInfoOptional.orElseThrow(() -> new SellException(ResultEnum.PRODUCT_NOT_EXIST)).getProductStatusEnum().equals(ProductStatusEnum.DOWN)){
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //更新
        ProductInfo productInfo = productInfoOptional.get();
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return repository.save(productInfo);
    }
}
