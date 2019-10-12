package com.daling.party.feign.to;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiwei.xue
 * @date 2019/5/8 13:20
 */
@Data
public class GoodGroupDetailTo implements Serializable {

    private static final long serialVersionUID = -3770399692377520092L;

    private int isInTimeline;

    private String preSaleYn;

    private int isForeignSupply;

    private int isInBond;

    private String goodsShowName;

    private Long goodsId;

    private List<String> skus;
}
