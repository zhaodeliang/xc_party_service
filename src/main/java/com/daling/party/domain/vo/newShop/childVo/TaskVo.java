package com.daling.party.domain.vo.newShop.childVo;

import java.io.Serializable;
import java.util.Date;

public class TaskVo implements Serializable, Comparable<TaskVo> {
	private static final long serialVersionUID = 6512703779925728375L;

	private Integer id;
	private Integer shopId;
	private Integer welfareId;
	private String taskCode;
	private Integer orderNum;
	private String taskDescUp;
	private String taskDescDown;
	private Integer medalNum;
	private String medalName;
	private String medalUnit;
	private String medalDesc;
	private String buttonPrompt;
	private Integer status;
	private String jumpUrl;
	private Integer jumpType;
	/**
	 * 生效开始时间
	 */
	private Date begTime;
	/**
	 * 生效结束时间
	 */
	private Date endTime;

	/**
	 * 背景色
	 */
	private String backColor;
	/**
	 * 按钮填充色
	 */
	private String buttonFillColor;
	/**
	 * 按钮字体颜色
	 */
	private String buttonFontColor;
	/**
	 * 标题字体颜色
	 */
	private String descUpFontColor;
	/**
	 * 内容颜色
	 */
	private String descDownFontColor;

	public Date getBegTime() {
		return begTime;
	}

	public void setBegTime(Date begTime) {
		this.begTime = begTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getBackColor() {
		return backColor;
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	public String getButtonFillColor() {
		return buttonFillColor;
	}

	public void setButtonFillColor(String buttonFillColor) {
		this.buttonFillColor = buttonFillColor;
	}

	public String getButtonFontColor() {
		return buttonFontColor;
	}

	public void setButtonFontColor(String buttonFontColor) {
		this.buttonFontColor = buttonFontColor;
	}

	public String getDescUpFontColor() {
		return descUpFontColor;
	}

	public void setDescUpFontColor(String descUpFontColor) {
		this.descUpFontColor = descUpFontColor;
	}

	public String getDescDownFontColor() {
		return descDownFontColor;
	}

	public void setDescDownFontColor(String descDownFontColor) {
		this.descDownFontColor = descDownFontColor;
	}

	public String getJumpUrl() {
		return jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		this.jumpUrl = jumpUrl;
	}

	public Integer getJumpType() {
		return jumpType;
	}

	public void setJumpType(Integer jumpType) {
		this.jumpType = jumpType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getWelfareId() {
		return welfareId;
	}

	public String getButtonPrompt() {
		return buttonPrompt;
	}

	public void setButtonPrompt(String buttonPrompt) {
		this.buttonPrompt = buttonPrompt;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public void setWelfareId(Integer welfareId) {
		this.welfareId = welfareId;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}


	public String getTaskDescUp() {
		return taskDescUp;
	}

	public void setTaskDescUp(String taskDescUp) {
		this.taskDescUp = taskDescUp;
	}

	public String getTaskDescDown() {
		return taskDescDown;
	}

	public void setTaskDescDown(String taskDescDown) {
		this.taskDescDown = taskDescDown;
	}

	public Integer getStatus() {
		return status;
	}

	public String getMedalDesc() {
		return medalDesc;
	}

	public void setMedalDesc(String medalDesc) {
		this.medalDesc = medalDesc;
	}

	public Integer getMedalNum() {
		return medalNum;
	}

	public void setMedalNum(Integer medalNum) {
		this.medalNum = medalNum;
	}

	public String getMedalName() {
		return medalName;
	}

	public void setMedalName(String medalName) {
		this.medalName = medalName;
	}

	public String getMedalUnit() {
		return medalUnit;
	}

	public void setMedalUnit(String medalUnit) {
		this.medalUnit = medalUnit;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public int compareTo(TaskVo o) {
		return o.getOrderNum().compareTo(this.orderNum);
	}
}
