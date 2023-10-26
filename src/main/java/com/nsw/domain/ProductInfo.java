package com.nsw.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nsw.enums.ProductStatusEnum;
import com.nsw.utils.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品表
 * @author nsw
 * @date 2018/9/17 15:18
 */
@Entity
@Data
@DynamicUpdate //时间自动更新
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = 4673723663872307275L;

    @Id
    private String productId;

    /** 名字. */
    private String productName;

    /** 单价. */
    private BigDecimal productPrice;

    /** 库存. */
    private Integer productStock;

    /** 描述. */
    private String productDescription;

    /** 小图. */
    private String productIcon;

    /** 状态, 0正常1下架. */
    private Integer productStatus = ProductStatusEnum.UP.getCode();//默认就是上架

    /** 类目编号. */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum() {
        return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
    }
}
