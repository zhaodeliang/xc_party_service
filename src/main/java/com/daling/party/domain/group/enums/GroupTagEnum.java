package com.daling.party.domain.group.enums;

/**
 * @author jiwei.xue
 * @date 2019/4/10 17:46
 */
public enum GroupTagEnum {

    SUP_SALE( "特卖"),
    USE_COUPON("可用券"),
    PRE_SALE( "预售"),
    OVERSEAS_DIRECT_MAIL( "海外直邮"),
    IN_BOND("保税");

    private String name;

    GroupTagEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
