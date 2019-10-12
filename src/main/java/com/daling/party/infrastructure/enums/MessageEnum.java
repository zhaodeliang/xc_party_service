package com.daling.party.infrastructure.enums;

public enum MessageEnum {

    valid_flag(0,"有效未处理"),
    invalid_flag(1,"失效已处理"),

    refund(0,"退款"),
    delivery(1,"发货"),
    group_and_cut(2, "成团和插队"),

    message_flag(6,"已发送"),
    ;

    private MessageEnum(int code, String name){
        this.code=code;
        this.name=name;
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

    private int code;
    private String name;
}
