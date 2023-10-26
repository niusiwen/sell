package com.nsw.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品（包含类目）
 * @author nsw
 * @date 2018/9/17 21:56
 */
@Data
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 7307736436511857021L;

    @JsonProperty("name") //返回前端是的字段名称就是name
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
