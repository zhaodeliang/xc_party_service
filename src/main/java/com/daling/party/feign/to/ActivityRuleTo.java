package com.daling.party.feign.to;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jiwei.xue
 * @date 2019/4/12 19:54
 */
@Data
public class ActivityRuleTo implements Serializable {

    private static final long serialVersionUID = 9148592991516681058L;

    /**
     * 满XX元 BigDecimal
     */
    private BigDecimal fullMoney;

    /**
     * 减XX元 BigDecimal
     */
    private BigDecimal subMoney;

}
