package com.daling.party.listener;

import com.alibaba.fastjson.JSONObject;
import com.daling.party.infrastructure.config.AmqpDeclareConfig;
import com.daling.party.service.newShop.INewShopTaskMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 新店主引导拥有vip消息监听
 */
@Component
@Slf4j
public class NewShopTaskHaveVipListener {

	@Autowired
	private INewShopTaskMsgService newShopTaskMsgService;

	@RabbitListener(queues = AmqpDeclareConfig.USER_ACCOUNT_QUEUE, concurrency = "15")
	public void onMessage(Message message) {
		log.info("newShopTaskHaveVipListener 消费者记录：{}", message);
		try {
			String value = new String(message.getBody(), "utf8");
			Map msg = JSONObject.parseObject(value, HashMap.class);
			String trackId = UUID.randomUUID().toString();
			msg.put("mstType",3);
			newShopTaskMsgService.dealMeg(msg, trackId);
		} catch (Exception e) {
			log.warn("newShopTaskHaveVipListener param illegal ", e);
		}
	}
}
