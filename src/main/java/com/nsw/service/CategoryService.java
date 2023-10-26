package com.nsw.service;

import com.nsw.domain.ProductCategory;

import java.util.List;

/**
 * @author nsw
 * @date 2018/9/17 10:54
 */
public interface CategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
