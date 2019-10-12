package com.daling.party.domain.vo.newShop.childVo;

import java.io.Serializable;
import java.util.Date;

public class FirstOrderVo implements Serializable {
	private static final long serialVersionUID = 6512703779925728375L;

	/**
	 * 图
	 */
	private String imgUrl;
	/**
	 * 标题名称
	 */
	private String title;

	/**
	 * 描述
	 */
	private String desc;

	/**
	 * 背景色
	 */
	private String backColor;
	/**
	 * 字体颜色
	 */
	private String priceColor;
	/**
	 * 按钮填充色
	 */
	private String FillColor;
	/**
	 * 按钮字体颜色
	 */
	private String FontColor;

	/**
	 * 截止时间
	 */
	private Date deadline;
	/**
	 * 状态
	 */
	private Integer status;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getBackColor() {
		return backColor;
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	public String getPriceColor() {
		return priceColor;
	}

	public void setPriceColor(String priceColor) {
		this.priceColor = priceColor;
	}

	public String getFillColor() {
		return FillColor;
	}

	public void setFillColor(String fillColor) {
		FillColor = fillColor;
	}

	public String getFontColor() {
		return FontColor;
	}

	public void setFontColor(String fontColor) {
		FontColor = fontColor;
	}

	public FirstOrderVo() {
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public FirstOrderVo(String imgUrl, String title, Integer status) {
		this.imgUrl = imgUrl;
		this.title = title;
		this.status = status;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
