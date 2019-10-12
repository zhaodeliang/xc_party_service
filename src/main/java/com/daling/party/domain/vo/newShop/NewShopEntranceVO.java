package com.daling.party.domain.vo.newShop;

import java.io.Serializable;

/**
 * @Auther : wangheming
 * @Date : 2019/07/22 16:31
 * @Description :新店主福利入口 vo
 */
public class NewShopEntranceVO implements Serializable {
    private static final long serialVersionUID = -8347856955506193069L;

    /**
     * 是否展示弹窗 0:显示 1不显示
     */
    private Integer isShowWindow;
    /**
     * 弹窗图片url地址
     */
    private String windowImgUrl;
    /**
     * 是否展示新店主福利入口 0:显示 1不显示
     */
    private Integer isShowEntrance;

    /**
     * 背景色
     */
    private String backColor;
    /**
     * 字体颜色
     */
    private String priceColor;

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

    public Integer getIsShowWindow() {
        return isShowWindow;
    }

    public void setIsShowWindow(Integer isShowWindow) {
        this.isShowWindow = isShowWindow;
    }

    public String getWindowImgUrl() {
        return windowImgUrl;
    }

    public void setWindowImgUrl(String windowImgUrl) {
        this.windowImgUrl = windowImgUrl;
    }

    public Integer getIsShowEntrance() {
        return isShowEntrance;
    }

    public void setIsShowEntrance(Integer isShowEntrance) {
        this.isShowEntrance = isShowEntrance;
    }
}
