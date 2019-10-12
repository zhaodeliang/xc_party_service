package com.daling.party.domain.group.enums;


/**
 * 新店主任务
 *
 * @author wangheming
 */
public enum NewShopTaskEnum {

	//福利状态
	welfare_valid(1, "有效"),
	welfare_invalid(2, "无效"),

	//奖章奖励状态
	medal_no_get(1, "未达成"), // 总奖章10自动发送
	medal_obtain(2, "已凑齐"),

	//开单状态
	first_order_init(0, "初始化"),
	first_order_obtain(1, "已获得"),
	first_order_overdue(2, "已过期"),

	//任务状态
	task_unfinished(0, "未完成"),
	task_finished(1, "已完成"),
	task_receive(2, "待领取"),

	//订单类型
	order_buy(1, "购买"),
	order_sale(2, "售卖"),

	other(99, "其他");
	;

	private int code;
	private String name;

	private NewShopTaskEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public static NewShopTaskEnum codeOf(int code) {
		for (NewShopTaskEnum memberTypeEnum : NewShopTaskEnum.values()) {
			if (memberTypeEnum.code == code) {
				return memberTypeEnum;
			}
		}
		return other;
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
