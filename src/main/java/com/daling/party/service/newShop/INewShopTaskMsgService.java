package com.daling.party.service.newShop;

import com.daling.party.infrastructure.model.ResultVO;

import java.util.Date;
import java.util.Map;

public interface INewShopTaskMsgService {

	/**
	 * 
	 * @param msg
	 * @param trackId
	 * @return
	 */
	boolean dealMeg(Map msg, String trackId);
}
