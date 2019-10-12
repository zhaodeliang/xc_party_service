package com.daling.party.infrastructure.enums;


/**
 * 可见类型
 *
 */
public enum VisibleTypeEnum {

	Whole(0, "全部","全部"),
	ShopNew(1, "无自购普单店主","新店主"),
	ShopOld(2, "已自购普单店主","老店主"),
	UserNew(3, "无开普单用户","新用户"),
	UserOld(4, "已开普单用户","老用户"),
	VipNew(5, "无开普单VIP","新VIP"),
	VipOld(6, "已开普单VIP","老VIP"),
	;
	
	private int code;
	private String name;
	private String desc;
	private VisibleTypeEnum(int code, String name, String desc) {
		this.code = code;
		this.name = name;
		this.desc = desc;
	}

	public static VisibleTypeEnum codeOf(int code) {
		for (VisibleTypeEnum memberTypeEnum : VisibleTypeEnum.values()) {
			if (memberTypeEnum.code == code) {
				return memberTypeEnum;
			}
		}
		return Whole;
	}
	
	public static VisibleTypeEnum nameOf(String name) {
		for (VisibleTypeEnum memberTypeEnum : VisibleTypeEnum.values()) {
			if (memberTypeEnum.name.equalsIgnoreCase(name)) {
				return memberTypeEnum;
			}
		}
		return Whole;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
