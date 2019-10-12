package com.daling.party.domain.vo.newShop.childVo;

import java.io.Serializable;

public class TitleVo implements Serializable {
	private static final long serialVersionUID = 6512703779925728375L;

	/**
	 * 背景图
	 */
	private String backImg;
	/**
	 * 标题名称
	 */
	private String titleName;

	/**
	 * 背景色
	 */
	private String backColor;
	/**
	 * 字体颜色
	 */
	private String fontColor;

	public String getBackColor() {
		return backColor;
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getBackImg() {
		return backImg;
	}

	public void setBackImg(String backImg) {
		this.backImg = backImg;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}

	@Override
	public String toString() {
		return "backImg:"+backImg+ ",titleName"+titleName;
	}
}
