package com.daling.party.service.newShop.impl;

import com.alibaba.fastjson.JSONObject;
import com.daling.common.redishelper.RedisHelper;
import com.daling.party.domain.constants.NewShopEnum;
import com.daling.party.domain.entity.NewShopTask;
import com.daling.party.domain.entity.NewShopWelfare;
import com.daling.party.domain.group.enums.NewShopTaskEnum;
import com.daling.party.domain.vo.newShop.childVo.TaskVo;
import com.daling.party.repository.dao.NewShopWelfareDao;
import com.daling.party.service.newShop.INewShopTaskMsgService;
import com.daling.party.service.newShop.INewShopTaskService;
import com.daling.party.utils.SmartDateUtil;
import com.daling.ucclient.clients.UserShopClient;
import com.daling.ucclient.pojo.Shop;
import com.daling.ucclient.pojo.User;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewShopTaskMsgServiceImpl implements INewShopTaskMsgService {

	public Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NewShopWelfareDao newShopWelfareDao;
	@Autowired
	private INewShopTaskService newShopTaskService;

	@Override
	@Transactional
	public boolean dealMeg(Map msg, String trackId) {
		Integer mstType = MapUtils.getInteger(msg, "mstType");
		if (null == mstType) {
			this.errMsg(msg, trackId, "mstType 为空");
		}
		if (1 == mstType) {// 精选
			this.selectedCallback(msg, trackId);
		}
		if (2 == mstType) {// 订单
			Long shopId = MapUtils.getLong(msg, "shopId");
			Long userId = MapUtils.getLong(msg, "userId");
			Shop shop = UserShopClient.getShop(shopId);
			Date createdDate = shop.getCreatedDate();
			if (null == shop || null == userId) {
				logger.info("params error,shopId{},trackId{}", shopId, trackId);
				this.errMsg(msg, trackId, "参数不全");
				return false;
			}
			boolean showTask = newShopTaskService.isShowNewShopTask(shopId, createdDate, trackId);
			if (!showTask) {
				logger.info("用户不符合条件", userId, trackId);
				return false;
			}
			Long orderDate = MapUtils.getLong(msg, "orderDate");
			if (null == orderDate) {
				this.errMsg(msg, trackId, "orderDate为空");
			}
			Date ordDate = new Date(orderDate);
			Map<String, NewShopTask> taskMap = this.getTaskMap(userId, shopId, ordDate, trackId);
			this.successSale(taskMap.get(NewShopEnum.saleAnOrder), msg, shop, trackId);
			this.useCoupon(taskMap.get(NewShopEnum.useSjjCoupon), msg, shop, trackId);
			this.haveShopOwner(taskMap.get(NewShopEnum.successRecruit), msg, shop, trackId);
			return true;
		}
		if (3 == mstType) {// vip
			this.haveVip(msg, trackId);
			return true;
		}
		if (4 == mstType) {// 商学院
			Long userId = MapUtils.getLong(msg, "userId");
			User user = UserShopClient.getUser(userId);
			Shop shop = UserShopClient.getShopByOwnerId(user.getId());
			Long dateTime = MapUtils.getLong(msg, "dateTime");
			if (null == dateTime) {
				logger.info("params error,shopId{},trackId{}", shop.getId(), trackId);
				this.errMsg(msg, trackId, "参数不全");
				return false;
			}
			newShopTaskService.finishTask(shop.getId(), NewShopEnum.sxyxskc, new Date(dateTime), trackId);
			return true;
		}
		return false;
	}

	private Map<String, NewShopTask> getTaskMap(Long userId, Long shopId, Date orderDate, String trackId) {
		Map<String, NewShopTask> taskMap = new HashMap<>();
		Map<String, TaskVo> hotMap = newShopTaskService.getHotConfigTaskInfo(orderDate, trackId);
		List<NewShopTask> tasks = newShopTaskService.getTasks(userId, shopId, hotMap, trackId);
		if (null == tasks || tasks.size() == 0) {
			return null;
		}
		for (NewShopTask task : tasks) {
			taskMap.put(task.getTaskCode(), task);
		}
		return taskMap;
	}

	// 记录错误日志 - 问题多则记录表
	private void errMsg(Map msg, String trackId, String errMsg) {
		StringBuilder logStr = new StringBuilder();
		String s = JSONObject.toJSONString(msg);
		logStr.append("trackId:");
		logStr.append(trackId);
		logStr.append(",errorMsg:{");
		logStr.append(errMsg+",");
		logStr.append("messageInfo=");
		logStr.append(s);
		logger.error(logStr.toString());
	}

	/**
	 * 分享精选回调发送奖章
	 * @param msg
	 */
	private void selectedCallback(Map msg, String trackId) {
		Long userId = MapUtils.getLong(msg, "userId");
		if (null == userId) {
			this.errMsg(msg, trackId, "精选回调userId为空");
			return;
		}
		Long dateTime = MapUtils.getLong(msg, "dateTime");
		User user = UserShopClient.getUser(userId);
		Shop shop = UserShopClient.getShopByOwnerId(user.getId());
		Map<String, NewShopTask> taskMap = this.getTaskMap(userId, shop.getId(), new Date(dateTime), trackId);
		NewShopTask task = taskMap.get(NewShopEnum.shareSelected);
		if (NewShopTaskEnum.task_finished.equals(task.getStatus())) return;
		newShopTaskService.finishTask(shop.getId(), NewShopEnum.shareSelected, new Date(dateTime), trackId);
	}

	private void useCoupon(NewShopTask task, Map msg, Shop shop, String trackId) {
		if (NewShopTaskEnum.task_finished.equals(task.getStatus())) return;
		Long userId = MapUtils.getLong(msg, "userId");
		Long ownerId = shop.getOwnerId();
		if (!userId.equals(ownerId)) {
			return;
		}
		Long orderDate = MapUtils.getLong(msg, "orderDate");
		if (null == orderDate) {
			this.errMsg(msg, trackId, "orderDate为空");
		}
		Double couponAmount = MapUtils.getDouble(msg, "couponAmount");
		if (null != couponAmount) {
			if (NewShopTaskEnum.task_finished.equals(task.getStatus())) return;
			// 获取券类型
			newShopTaskService.finishTask(shop.getId(), task.getTaskCode(), new Date(orderDate), trackId);
		}
		try {
			this.firstOrder(shop, orderDate, trackId); //开单
		} catch (Exception e) {
			this.errMsg(msg, trackId, "记录开单状态异常");
		}
	}

	// 成功售出一笔订单 -- 出售订单
	private void successSale(NewShopTask task, Map msg, Shop shop, String trackId) {
		if (NewShopTaskEnum.task_finished.equals(task.getStatus())) {
			return;
		}
		Long ownerId = shop.getOwnerId();
		Long userId = MapUtils.getLong(msg, "userId");
		if (userId.equals(ownerId)) {
			return;
		}
		Long orderDate = MapUtils.getLong(msg, "orderDate");
		newShopTaskService.finishTask(shop.getId(), task.getTaskCode(), new Date(orderDate), trackId);
	}

	// 拥有一位店铺VIP -- 有接口
	private void haveVip(Map msg, String trackId) {

//		String inviteCode = MapUtils.getString(msg, "inviteCode");
//		Shop shop = UserShopClient.getShopByInviteCode(inviteCode);
//		Date createdDate = shop.getCreatedDate();
//		Long shopId = shop.getId();
//		Long ownerId = shop.getOwnerId();
//		if (null == shop) {
//			logger.info("can not find shop info,shopId{},trackId{}", shopId, trackId);
//			this.errMsg(msg, trackId, "根据inviteCode查无shop信息");
//			return;
//		}
//		if (!newShopTaskService.isShowNewShopTask(shopId, createdDate, trackId)) {
//			Long userId = MapUtils.getLong(msg, "userId");
//			if (null == userId) {
//				logger.info("无vip用户，用户ID为空", userId, trackId);
//				return;
//			}
//		}

		Long shopId = MapUtils.getLong(msg, "shopId");
		String eventTagType = MapUtils.getString(msg, "eventTagType");
		if(!"pay_order".equals(eventTagType)){
			return;
		}
		Shop shop = UserShopClient.getShop(shopId);
		Date createdDate = shop.getCreatedDate();
		Long ownerId = shop.getOwnerId();
		if (null == shop) {
			logger.info("can not find shop info,shopId{},trackId{}", shopId, trackId);
			this.errMsg(msg, trackId, "根据inviteCode查无shop信息");
			return;
		}
		if (!newShopTaskService.isShowNewShopTask(shopId, createdDate, trackId)) {
			Long userId = MapUtils.getLong(msg, "userId");
			if (null == userId) {
				logger.info("无vip用户，用户ID为空", userId, trackId);
				return;
			}
		}
		Map<String, NewShopTask> taskMap = this.getTaskMap(ownerId, shopId, new Date(), trackId);
		if (NewShopTaskEnum.task_finished.equals(taskMap.get(NewShopEnum.haveVip).getStatus())) {
			return;
		}
		newShopTaskService.finishTask(shopId, NewShopEnum.haveVip, new Date(), trackId);
	}

	// 成功拉取一个店主
	private void haveShopOwner(NewShopTask task, Map msg, Shop shop, String trackId) {
		if (NewShopTaskEnum.task_finished.equals(task.getStatus())) return;
		Long userId = MapUtils.getLong(msg, "userId");
		Long ownerId = shop.getOwnerId();
		if (userId.equals(ownerId)) return;
		Integer orderType = MapUtils.getInteger(msg, "orderType");
		if (orderType.intValue() != 1) return; // 非精选
		Date orderDate = new Date(MapUtils.getLong(msg, "orderDate"));
		newShopTaskService.finishTask(task.getShopId(), task.getTaskCode(), orderDate, trackId);
	}

	// 开单奖励
	private void firstOrder(Shop shop, Long orderTime, String trackId) throws Exception {
		NewShopWelfare welfare = null;
		try (Jedis jedis = RedisHelper.getJedis()) {
			welfare = newShopTaskService.getWelfareByShopId(shop.getId(), jedis, trackId);
		}
		if (welfare.getPrizeOne() == NewShopTaskEnum.first_order_obtain.getCode() ||
				welfare.getPrizeTwo() == NewShopTaskEnum.first_order_obtain.getCode() ||
				welfare.getPrizeTwo() == NewShopTaskEnum.first_order_overdue.getCode()) {
			return;
		}
		Date shopTime = welfare.getShopTime();
		Date addTwoDay = SmartDateUtil.getAddDayBeginTime(2, shopTime);
		Date addFivDay = SmartDateUtil.getAddDayBeginTime(5, shopTime);
		Date oTime = new Date(orderTime);
		if (welfare.getPrizeTwo() == NewShopTaskEnum.first_order_init.getCode()) {
			if (oTime.after(addFivDay)) {
				welfare.setPrizeOne(NewShopTaskEnum.first_order_overdue.getCode());
				welfare.setPrizeTwo(NewShopTaskEnum.first_order_overdue.getCode());
			} else if (oTime.after(addTwoDay)) {
				welfare.setPrizeOne(NewShopTaskEnum.first_order_overdue.getCode());
				welfare.setPrizeTwo(NewShopTaskEnum.first_order_obtain.getCode());
			} else {
				welfare.setPrizeOne(NewShopTaskEnum.first_order_obtain.getCode());
			}
		}
		newShopWelfareDao.updateWelfare(welfare);
		newShopTaskService.freshWelfareCacheWithJedis(welfare, trackId);
	}
}
