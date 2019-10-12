package com.daling.party.infrastructure.enums;

public enum AppEnum {

    APPSTORE("appstore","线上版"),
    HUIBANG("huibang","备份版");



    private AppEnum(String code,String name){
        this.code=code;
        this.name=name;
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

    private String code;
    private String name;
}
