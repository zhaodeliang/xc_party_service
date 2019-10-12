package com.daling.party.domain.group.enums;

import lombok.Getter;

/**
 * Created by lilindong on 2019/4/11.
 */
@Getter
public enum GroupJoinStatusEnum {

    NO_EXITS(0, "团不存在"),
    NO_START(1, "团未开始"),
    PROCESSING(2, "正在拼团"),
    GROUP_SUCCESS(3, "已成团，但未达到人数上限，但未达到拼团结束时间"),
    GROUP_SUCCESS_END(4, "已成团，但人数没有到达上限，且团已经结束"),
    GROUP_SUCCESS_UPPER(5, "已成团，且已到达人数上限，但未达到拼团结束时间"),
    GROUP_SUCCESS_END_UPPER(6, "已成团，且已到达人数上限，而且拼团已经结束"),
    GROUP_FAIL(7, "拼团失败"),
    JOIN_GROUP_FAIL(700, "参团失败")
    ;

    private int code;
    private String name;

    GroupJoinStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static GroupJoinStatusEnum codeOf(int code) {
        for (GroupJoinStatusEnum groupStatusEnum : values()) {
            if (groupStatusEnum.code == code) {
                return groupStatusEnum;
            }
        }
        return null;
    }
}
