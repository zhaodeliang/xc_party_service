package com.daling.party.feign.to;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiwei.xue
 * @date 2019/4/12 20:42
 */
@Data
public class GroupRuleTo implements Serializable {

    private static final long serialVersionUID = 2596925056644939751L;

    /**
     * 成团人数 团下限
     */
    private Integer groupNumber;

    /**
     * 团最大人数 团上限
     */
    private Integer maxGroupNumber;

    /**
     * 当前活动的时长
     */
    private Integer validity;
}
