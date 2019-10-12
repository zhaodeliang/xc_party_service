package com.daling.party.infrastructure.enums;

import lombok.Getter;

@Getter
public enum BooleanEnum {
    True(1),
    Six(6),
    False(0);

    private Integer code;

    BooleanEnum(Integer code) {
        this.code = code;
    }
}
