package com.daling.party.domain.entity;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangheming
 * @version 2019年7月23日 11:31
 */

@Table(name = "T_NEW_SHOP_TASK_SNAPSHOT")
public class NewShopTask implements Serializable {

	private static final long serialVersionUID = 3125357083262357267L;

	private long id;
	private long shopId;
	private long userId;
	private long welfareId;
	private String taskCode;
	private String taskDescUp;
	private String taskDescDown;
	private String taskUrl;
	private int medalNum;
	private int status;
	private Date finishedTime;
	private Date createDate;
	private Date modifyDate;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public long getWelfareId() {
		return welfareId;
	}

	public void setWelfareId(long welfareId) {
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

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public int getMedalNum() {
		return medalNum;
	}

	public void setMedalNum(int medalNum) {
		this.medalNum = medalNum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
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
