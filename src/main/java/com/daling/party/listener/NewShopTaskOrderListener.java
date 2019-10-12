package com.daling.party.listener;

import com.alibaba.fastjson.JSONObject;
import com.daling.party.service.newShop.INewShopTaskMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 新店主引导订单消息监听
 */
@Service("newShopTaskOrderListener")
@Slf4j
public class NewShopTaskOrderListener implements MessageListener {

	@Autowired
	private INewShopTaskMsgService newShopTaskMsgService;

	@Override
	public void onMessage(Message message) {
		log.info("newShopTaskListener 消费者记录：{}", message);
		try {
			String value = new String(message.getBody(), "utf8");
			Map msg = JSONObject.parseObject(value, HashMap.class);
			String trackId = UUID.randomUUID().toString();
			newShopTaskMsgService.dealMeg(msg, trackId);
		} catch (Exception e) {
			log.warn("newShopTaskListener param illegal ", e);
			// 处理异常逻辑 - 保存消息
		}
	}
}
