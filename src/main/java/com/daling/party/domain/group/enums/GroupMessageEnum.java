package com.daling.party.domain.group.enums;

/**
 * @author jiwei.xue
 * @date 2019/4/11 21:35
 */
public enum GroupMessageEnum {

    JOIN_SUCCESS("group_shopping_friend", "参团成功"),
    GROUP_SUCCESS("group_shopping_success", "成团成功"),
    GROUP_SHOPPING_SHARE_UNFINISHED_REMAINING_2HOURS("group_shopping_share_unfinished_remaining_2hours", "分享团-当前团截止前2小时，且未达到成团人数"),
    GROUP_SHOPPING_SELF_UNFINISHED_REMAINING_2HOURS("group_shopping_self_unfinished_remaining_2hours", "自购团-当前团截止前2小时，且未达到成团人数"),
    GROUP_SHOPPING_FINISHED_REMAINING_2HOURS("group_shopping_finished_remaining_2hours", "当前团截止前2小时，且已达到成团人数"),
    GROUP_SHOPPING_UNFINISHED_6HOURS("group_shopping_unfinished_6hours", "当前团开团后6小时，且未达到成团人数"),
    GROUP_SHOPPING_FAIL_CODE("group_shopping_fail", "拼团失败提醒"),

    ;

    private String code;
    private String name;

    GroupMessageEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getName(String code) {
        GroupMessageEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            GroupMessageEnum item = var1[var3];
            if (item.getCode().equals(code)) {
                return item.getName();
            }
        }
        return null;
    }
}
