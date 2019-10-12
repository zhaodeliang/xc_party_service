package com.daling.party.domain.entity;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangheming
 * @version 2019年7月23日 11:31
 */

@Table(name = "T_NEW_SHOP_WELFARE")
public class NewShopWelfare implements Serializable {

	private static final long serialVersionUID = 4989772211487632387L;

	private long id;

	/**
	 * 用户ID
	 */
	private long userId;

	/**
	 * 店主ID
	 */
	private long shopId;
	/**
	 * 店主时间
	 */
	private Date shopTime;
	/**
	 * 是否有效:1有效2无效
	 */
	private int status;
	/**
	 * 奖章福利状态
	 */
	private int welfareStatus;
	/**
	 * 奖章数量
	 */
	private int medalNum;
	/**
	 * 开单奖励1状态
	 */
	private int prizeOne;
	/**
	 * 开单奖励2状态
	 */
	private int prizeTwo;
	private Date createDate;
	private Date modifyDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getShopTime() {
		return shopTime;
	}

	public void setShopTime(Date shopTime) {
		this.shopTime = shopTime;
	}

	public int getWelfareStatus() {
		return welfareStatus;
	}

	public void setWelfareStatus(int welfareStatus) {
		this.welfareStatus = welfareStatus;
	}

	public int getMedalNum() {
		return medalNum;
	}

	public void setMedalNum(int medalNum) {
		this.medalNum = medalNum;
	}

	public int getPrizeOne() {
		return prizeOne;
	}

	public void setPrizeOne(int prizeOne) {
		this.prizeOne = prizeOne;
	}

	public int getPrizeTwo() {
		return prizeTwo;
	}

	public void setPrizeTwo(int prizeTwo) {
		this.prizeTwo = prizeTwo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
}
