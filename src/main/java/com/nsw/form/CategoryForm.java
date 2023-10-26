package com.nsw.form;

import lombok.Data;

/**
 * @author nsw
 * @date 2018/9/27 11:00
 */
@Data
public class CategoryForm {

    private Integer categoryId;

    /** 类目名字. */
    private String categoryName;

    /** 类目编号. */
    private Integer categoryType;
}
