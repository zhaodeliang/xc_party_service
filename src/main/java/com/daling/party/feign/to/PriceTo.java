package com.daling.party.feign.to;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiwei.xue
 * @date 2019/4/12 19:55
 */
@Data
public class PriceTo implements Serializable {

    private static final long serialVersionUID = -2831668284649734091L;

    /**
     * 活动编码
     */
    private String actCode;

    /**
     * 版本
     */
    private String v;

    /**
     * 短标题
     */
    private String shortTitle;

    /**
     * 市场价
     */
    private float market;

    /**
     * 销售价
     */
    private float sales;

    /**
     * 佣金
     */
    private float benefit;

    /**
     * 金币
     */
    private int gold;

    /**
     * 金币可见用户（1、新用户2、老用户4、新店主8、老店主 16、新vip 32、老vip）
     */
    private int goldT;

}
