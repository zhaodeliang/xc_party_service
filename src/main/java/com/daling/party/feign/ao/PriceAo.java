package com.daling.party.feign.ao;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jiwei.xue
 * @date 2019/5/5 17:40
 * 价格体系-价格参数类
 */
@Data
public class PriceAo implements Serializable {

    private static final long serialVersionUID = -893204501236647503L;

    /**
     * sku
     */
    private String sku;

    /**
     * 价格类型 1、一般售价
     */
    private int priceType;

    /**
     * 活动价
     */
    private BigDecimal salePrice;

    /**
     * 市场价
     */
    private BigDecimal market;

    /**
     * 佣金
     */
    private BigDecimal benefit;
}
