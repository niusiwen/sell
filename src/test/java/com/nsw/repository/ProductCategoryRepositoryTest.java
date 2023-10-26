package com.nsw.repository;

import com.nsw.domain.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author nsw
 * @date 2018/9/16 23:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOneTest(){
        //使用getOne
        //ProductCategory productCategory = productCategoryRepository.getOne(1);
        //SpringDataJPA 2.xx 版本之后findOne方法已经变了 由findById(id).get()来查询
        //ProductCategory productCategory = productCategoryRepository.findOne(1);
        ProductCategory productCategory = productCategoryRepository.findById(1).get();

        System.out.println(productCategory.getCategoryId()+","+productCategory.getCategoryName()+","+productCategory.getCategoryType());
    }

    @Test
    @Transactional  //测试后让数据库不保存测试数据
    public void saveTest(){
        /*ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(3);*/
        ProductCategory productCategory = new ProductCategory("女生最爱", 4);
        productCategoryRepository.save(productCategory);
        //使用断言类判断测试是否达到预期
        Assert.assertNotNull(productCategory);
//        Assert.assertNotEquals(null, productCategory); //两者效果一样
    }

    @Test
    public void updateTest(){
        //更新的时候同样使用save方法，只不过要传入要更新数据的主键id
        ProductCategory productCategory = productCategoryRepository.findById(2).get();
        productCategory.setCategoryName("男生最爱");
        //productCategory.setCategoryType(3);
        productCategoryRepository.save(productCategory);
    }

    @Test
    public void findByCategoryTypeInTest(){
        List<Integer> list = Arrays.asList(2,3,4);

        List<ProductCategory> result = productCategoryRepository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,result.size());
    }
}