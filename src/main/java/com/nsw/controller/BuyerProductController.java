package com.nsw.controller;

import com.nsw.VO.ProductInfoVO;
import com.nsw.VO.ProductVO;
import com.nsw.VO.ResultVO;
import com.nsw.domain.ProductCategory;
import com.nsw.domain.ProductInfo;
import com.nsw.service.CategoryService;
import com.nsw.service.ProductService;
import com.nsw.utils.ResultVoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家商品
 * @author nsw
 * @date 2018/9/17 21:29
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
//    @Cacheable(cacheNames = "product", key = "123") //缓存注解，将查询的结果放入redis缓存中
//    @Cacheable(cacheNames = "product", key = "#sellerId", condition = "#sellerId.length()>3", unless = "#result.getCode() != 0")
    public ResultVO list() {
        //1. 查询所有的上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();
        //2. 查询类目（一次性查询）
//        List<Integer> categoryTypeList = new ArrayList<>();
        //传统的方法
//        for (ProductInfo productInfo : productInfoList) {
//            categoryTypeList.add(productInfo.getCategoryType());
//        }
        //精简方法（jDK8, lambda）
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
        //3. 数据拼装
        List<ProductVO> productVOList = new ArrayList<>();
        productCategoryList.forEach(item->{
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(item.getCategoryType());
            productVO.setCategoryName(item.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            productInfoList.forEach(productInfo -> {
                if (productInfo.getCategoryType().equals(item.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            });
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        });

        /*ResultVO resultVO = new ResultVO();
        resultVO.setData(productVOList);
        resultVO.setCode(0);
        resultVO.setMsg("成功");*/

        return ResultVoUtil.success(productVOList);
    }


}
