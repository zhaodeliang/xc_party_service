package com.daling.party.domain.group.enums;

/**
 * @author jiwei.xue
 * @date 2019/4/9 22:02
 */
public enum GroupStatusEnum {

    DRAFT(0, "草稿团"),
    SUCCESS(1, "拼团成功"),
    FAIL(2, "拼团失败"),
    PROCESSING(3, "拼团进行中");

    private int code;
    private String name;

    GroupStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static GroupStatusEnum codeOf(int code) {
        for (GroupStatusEnum groupStatusEnum : values()) {
            if (groupStatusEnum.code == code) {
                return groupStatusEnum;
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
