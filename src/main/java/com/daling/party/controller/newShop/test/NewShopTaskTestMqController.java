package com.daling.party.controller.newShop.test;

import com.alibaba.fastjson.JSONObject;
import com.daling.party.controller.dto.UserDto;
import com.daling.party.domain.vo.newShop.NewShopEntranceVO;
import com.daling.party.infrastructure.base.BaseController;
import com.daling.party.infrastructure.model.CommonConstant;
import com.daling.party.infrastructure.utils.GenUserUtil;
import com.daling.party.infrastructure.utils.XcHeadWrapper;
import com.daling.party.service.coupon.SendCouponService;
import com.daling.party.service.newShop.INewShopTaskService;
import com.daling.party.service.newShop.test.NewShopTaskTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 新店主引导任务
 */
@RequestMapping("newShop/msg")
@Controller
@Slf4j
public class NewShopTaskTestMqController extends BaseController {

	@Autowired
	private SendCouponService sendCouponService;

	@Autowired
	private AmqpTemplate new_shop_task_order_sender;

	/**
	 * 发券mq
	 */
	@RequestMapping("sendCoupon.do")
	XcHeadWrapper<NewShopEntranceVO> sendCoupon(UserDto user, String trackId) {
		trackId = StringUtils.isBlank(trackId) ? UUID.randomUUID().toString() : trackId;
		XcHeadWrapper<NewShopEntranceVO> jsonWrapper = initJsonHeaderWrapper(trackId);
		user = GenUserUtil.gen(trackId, user);
		String couponCode = "new_shop_welfare_coupon_code"; //券编码
		String couponQuantity = "new_shop_welfare_coupon_quantity"; // 券数量
		String code = this.getHotConf(couponCode, trackId);
		String quantity = this.getHotConf(couponQuantity, trackId);
		sendCouponService.sendCouponToNewShop(user.getId(), code, Integer.valueOf(quantity));
		return jsonWrapper;
	}

	/**
	 * 订单回调
	 */
	@RequestMapping("payCallBack.do")
	XcHeadWrapper<NewShopEntranceVO> payCallBack(UserDto user, String trackId) {
		trackId = StringUtils.isBlank(trackId) ? UUID.randomUUID().toString() : trackId;
		XcHeadWrapper<NewShopEntranceVO> jsonWrapper = initJsonHeaderWrapper(trackId);
		user = GenUserUtil.gen(trackId, user);
		Map msg = new HashMap();
		msg.put("mstType ",1);
		msg.put("userId ",user.getId());
		String s = JSONObject.toJSONString(msg);
		new_shop_task_order_sender.convertAndSend(s);
		return jsonWrapper;
	}

	private String getHotConf(String key, String trackId) {
		String value = CommonConstant.getHotConfigValue(key, "").trim();
		if (StringUtils.isEmpty(value)) {
			value = NewShopTaskTest.getStr(trackId, key);
		}
		return value;
	}


}
