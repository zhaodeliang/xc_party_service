package com.daling.party.service.coupon;

import com.alibaba.fastjson.JSONObject;
import com.daling.party.infrastructure.config.AmqpDeclareConfig;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

@Service("sendCouponService")
public class SendCouponService {

	Logger log = LoggerFactory.getLogger(SendCouponService.class);

//	@Autowired
//	private AmqpTemplate public_message_sender;
	@Resource
	private RabbitTemplate rabbitTemplate;

	/**
	 * 新店主任务 机器奖章发券
	 *
	 * @param userId
	 * @param quantity
	 */
	public void sendCouponToNewShop(long userId, String code, Integer quantity) {
		Map<String, Object> data = Maps.newHashMap();
		data.put("user_id", userId);
		data.put("quantity", quantity);
		data.put("opt_time", System.currentTimeMillis());
		this.send(code, data);
	}


	private void send(String code, Map<String, Object> data) {
		try {
			Map<String, Object> map = Maps.newHashMap();
			map.put("track_id", UUID.randomUUID().toString());
			map.put("event_time", System.currentTimeMillis());
			map.put("code", code);
			map.put("body", data);
			String msg = JSONObject.toJSONString(map);
			//String msg = Jackson2Helper.toJsonString(map);
			//public_message_sender.convertAndSend(msg);
			rabbitTemplate.convertAndSend(AmqpDeclareConfig.GROUP_SHOPPING_MESSAGE_EXCHANGE, null, msg);
		} catch (AmqpException e) {
			log.error(e.getMessage(), e);
		}
	}
}
