package com.daling.party.feign.to;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiwei.xue
 * @date 2019/4/12 19:52
 */
@Data
public class ActivityTo implements Serializable {

    private static final long serialVersionUID = -3183791508063347237L;

    /**
     * 活动编码
     */
    private String activityCode;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动简称
     */
    private String activityShortName;

    /**
     * 活动类型 1：直降, 2：秒杀 3：阶梯满减 4:阶梯满折 5：N元任选 6：每满减（每满多少元减多少元）
     */
    private Integer activityType;

    /**
     * 活动规则
     */
    private List<ActivityRuleTo> activityRuleDtoList;

    /**
     * 活动规则
     */
    private String activityRule;

    /**
     * 预热时间
     */
    private Long preheatTime;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 活动状态 5：预热中；6：活动中；7：已结束
     */
    private Integer activityStatus;

    /**
     * 商品详情
     */
    private PriceTo priceRedisModel;
}
