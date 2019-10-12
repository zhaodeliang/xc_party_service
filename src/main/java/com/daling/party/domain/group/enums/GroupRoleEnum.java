package com.daling.party.domain.group.enums;

/**
 * @author jiwei.xue
 * @date 2019/4/10 17:46
 */
public enum GroupRoleEnum {

    HEAD(1, "团长"),
    MEMBER(2, "成员"),
    INTRUDER(3, "插入者");

    private int code;
    private String name;

    GroupRoleEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static GroupRoleEnum codeOf(int code) {
        for (GroupRoleEnum groupRoleEnum : values()) {
            if (groupRoleEnum.code == code) {
                return groupRoleEnum;
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
