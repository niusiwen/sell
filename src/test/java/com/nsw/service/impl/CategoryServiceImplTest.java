package com.nsw.service.impl;

import com.nsw.domain.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author nsw
 * @date 2018/9/17 11:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryServiceImpl categoryservice;

    @Test
    public void findOne() {
        ProductCategory productCategory = categoryservice.findOne(1);
        Assert.assertEquals(new Integer(1), productCategory.getCategoryId());
    }

    @Test
    public void findAll() {
        List<ProductCategory> list = categoryservice.findAll();
        Assert.assertNotEquals(0, list.size());
    }

    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list = Arrays.asList(2,3,4);
        List<ProductCategory> result = categoryservice.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,result.size());
    }

    @Test
    public void save() {
        ProductCategory productCategory = new ProductCategory("女生专属", 4);
        ProductCategory result = categoryservice.save(productCategory);
        Assert.assertNotNull(result);
    }
}