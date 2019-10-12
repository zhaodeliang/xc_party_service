package com.daling.party.domain.group.enums;

/**
 * @author jiwei.xue
 * @date 2019/4/10 17:46
 */
public enum GroupTypeEnum {

    SHARE_GROUP(1, "分享团"),
    BUY_GROUP(2, "自购团");

    private int code;
    private String name;

    GroupTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static GroupTypeEnum codeOf(int code) {
        for (GroupTypeEnum groupTypeEnum : values()) {
            if (groupTypeEnum.code == code) {
                return groupTypeEnum;
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
