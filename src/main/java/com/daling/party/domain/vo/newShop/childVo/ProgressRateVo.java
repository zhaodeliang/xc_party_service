package com.daling.party.domain.vo.newShop.childVo;

import java.io.Serializable;

public class ProgressRateVo implements Serializable {

	private static final long serialVersionUID = -846090379823795223L;
	/**
	 * 标题
	 */
	private String title;

	/**
	 * 描述
	 */
	private String desc;

	/**
	 * 奖章数量
	 */
	private Integer medalNum;
	/**
	 * 奖章名称
	 */
	private String medalName;

	/**
	 * 奖章单位
	 */
	private String medalUnit;
	/**
	 * 奖励状态
	 */
	private Integer status;

	/**
	 * 背景图
	 */
	private String backImg;

	/**
	 * 奖励图
	 */
	private String prizeImg;

	/**
	 * 背景色
	 */
	private String backColor;

	/**
	 * 标题字体颜色
	 */
	private String titleFontColor;

	/**
	 * 内容颜色
	 */
	private String descFontColor;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMedalUnit() {
		return medalUnit;
	}

	public void setMedalUnit(String medalUnit) {
		this.medalUnit = medalUnit;
	}

	public String getPrizeImg() {
		return prizeImg;
	}

	public void setPrizeImg(String prizeImg) {
		this.prizeImg = prizeImg;
	}

	public String getBackColor() {
		return backColor;
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	public String getTitleFontColor() {
		return titleFontColor;
	}

	public void setTitleFontColor(String titleFontColor) {
		this.titleFontColor = titleFontColor;
	}

	public String getDescFontColor() {
		return descFontColor;
	}

	public void setDescFontColor(String descFontColor) {
		this.descFontColor = descFontColor;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBackImg() {
		return backImg;
	}

	public void setBackImg(String backImg) {
		this.backImg = backImg;
	}
}
