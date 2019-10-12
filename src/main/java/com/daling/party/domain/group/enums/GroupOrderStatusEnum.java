package com.daling.party.domain.group.enums;

/**
 * @author jiwei.xue
 * @date 2019/4/16 17:25
 */
public enum  GroupOrderStatusEnum {

    PAY_SUCCESS(1, "支付成功"),
    WAIT_DELIVER(2, "待发货");


    private int code;
    private String name;

    GroupOrderStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static GroupOrderStatusEnum codeOf(int code) {
        for (GroupOrderStatusEnum groupOrderStatusEnum : values()) {
            if (groupOrderStatusEnum.code == code) {
                return groupOrderStatusEnum;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
