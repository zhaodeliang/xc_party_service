package com.daling.party.service.newShop.impl;

import com.alibaba.fastjson.JSONObject;
import com.daling.common.redishelper.RedisHelper;
import com.daling.party.controller.dto.UserDto;
import com.daling.party.domain.constants.NewShopEnum;
import com.daling.party.domain.entity.NewShopTask;
import com.daling.party.domain.entity.NewShopWelfare;
import com.daling.party.domain.group.enums.NewShopTaskEnum;
import com.daling.party.domain.vo.newShop.NewShopEntranceVO;
import com.daling.party.domain.vo.newShop.NewShopModuleVO;
import com.daling.party.domain.vo.newShop.childVo.FirstOrderVo;
import com.daling.party.domain.vo.newShop.childVo.ProgressRateVo;
import com.daling.party.domain.vo.newShop.childVo.TaskVo;
import com.daling.party.domain.vo.newShop.childVo.TitleVo;
import com.daling.party.infrastructure.model.CommonConstant;
import com.daling.party.infrastructure.model.ResultVO;
import com.daling.party.infrastructure.utils.DateTimeTool;
import com.daling.party.repository.dao.NewShopTaskDao;
import com.daling.party.repository.dao.NewShopWelfareDao;
import com.daling.party.service.coupon.SendCouponService;
import com.daling.party.service.newShop.INewShopTaskService;
import com.daling.party.service.newShop.test.NewShopTaskTest;
import com.daling.party.utils.SmartDateUtil;
import com.daling.ucclient.clients.UserShopClient;
import com.daling.ucclient.pojo.Shop;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.*;

@Service
public class NewShopTaskServiceImpl implements INewShopTaskService {
	public Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NewShopTaskDao newShopTaskDao;
	@Autowired
	private NewShopWelfareDao newShopWelfareDao;
	@Autowired
	private SendCouponService sendCouponService;
	private static final int ex = 72 * 60 * 60; // 缓存时间

	@Override
	public ResultVO<NewShopEntranceVO> isShowEntrance(UserDto user, String trackId) {
		ResultVO<NewShopEntranceVO> resultVO = new ResultVO<>();
		NewShopEntranceVO result = new NewShopEntranceVO();
		Date currentTime = new Date();
		Long shopId = user.getShopId();
		if (null == shopId) {
			logger.info("null of shopId,userId{},trackId{}", user.getId(), trackId);
			return resultVO.format(false, "非店主", result);
		}
		Shop shop = UserShopClient.getShop(shopId);
		if (null == shop) {
			logger.info("can not find shop info,userId{},trackId{}", user.getId(), trackId);
			return resultVO.format(false, "无店铺信息", result);
		}

		// 热配时间
		boolean showTask = this.isShowNewShopTask(shopId, shop.getCreatedDate(), trackId);
		if (!showTask) {
			result.setIsShowEntrance(1);
			result.setIsShowWindow(1);
			return resultVO.format(true, "success", result);
		}

		// 是否首次进入店铺
		Date createdDate = shop.getCreatedDate();
		Date invalidTime = SmartDateUtil.getAddDayBeginTime(30, createdDate);
		try (Jedis jedis = RedisHelper.getJedis()) {
			String keyStr = NewShopEnum.cacheBaseKey + NewShopEnum.firstLogin + shopId;
			Boolean exists = jedis.exists(keyStr);
			if (!exists) {
				int seconds = (int) (invalidTime.getTime() - currentTime.getTime()) / 1000;
				jedis.setex(keyStr, seconds, "1");
				String winImgUrl = this.getHotConf(NewShopEnum.welfareWinUrl, trackId);
				result.setWindowImgUrl(winImgUrl);
				result.setIsShowWindow(0);
				this.initNewShopTask(user.getId(), shop, jedis, trackId);
			}
			if (exists) {
				result.setIsShowWindow(1);
			}
		}
		return resultVO.format(true, "成功", result);
	}

	/**
	 * 获取A B C D模块信息
	 *
	 * @param user
	 * @param trackId
	 * @return
	 */
	@Override
	public ResultVO<NewShopModuleVO> getModuleInfo(UserDto user, String trackId) throws Exception {
		ResultVO<NewShopModuleVO> resultVO = new ResultVO<>();
		NewShopModuleVO result = new NewShopModuleVO();
		result.setPartMove(0);

		// 标题
		String titleVoStr = this.getHotConf(NewShopEnum.welfareTitleKey, trackId);
		TitleVo titleVo = JSONObject.parseObject(titleVoStr, TitleVo.class);
		result.setTitleVo(titleVo);

		// 福利
		NewShopWelfare welfare = this.getWelfare(user, trackId);
		if (null == welfare) {
			return resultVO.format(false, "查询福利信息异常", result);
		}

		// 开单
		List<FirstOrderVo> firstOrderVos = this.getFirstOrderVo(welfare, result, trackId);
		// String firstOrderTitleStr = this.getHotConf(NewShopEnum.firstOrderTitle, trackId);
		result.setFirstOrderVos(firstOrderVos);
		// result.setFirstOrderTitle(firstOrderTitleStr);

		// 奖章进度
		int medalNum = welfare.getMedalNum();
		int medalStatus = welfare.getWelfareStatus();
		String progressRateStr = this.getHotConf(NewShopEnum.progressRate, trackId);
		ProgressRateVo progressRateVo = JSONObject.parseObject(progressRateStr, ProgressRateVo.class);
		progressRateVo.setMedalNum(medalNum);
		progressRateVo.setStatus(medalStatus);
		result.setProgressRateVo(progressRateVo);

		// 奖励弹层图
		String welfarePopUp = this.getHotConf(NewShopEnum.welfarePopupImg, trackId);
		result.setPopupImgUrl(welfarePopUp);

		return resultVO.format(true, "成功", result);
	}

	/**
	 * 获取任务显示列表
	 *
	 * @param user
	 * @param trackId
	 * @return
	 */
	@Override
	public ResultVO<List<TaskVo>> getTaskList(UserDto user, String trackId) {
		ResultVO<List<TaskVo>> resultVO = new ResultVO<>();
		List<TaskVo> taskVos = new ArrayList<>();
		Long userId = user.getId();
		Long shopId = user.getShopId();
		if (null == shopId || shopId.longValue() <= 0) {
			return resultVO.format(false, "shopId为空");
		}
		// 热配任务内容
		Map<String, TaskVo> taskMap = this.getHotConfigTaskInfo(new Date(), trackId);
		// 福利
		List<NewShopTask> taskList = this.getTasks(userId, shopId, taskMap, trackId);
		if (CollectionUtils.isEmpty(taskList)) {
			return resultVO.format(false, "查询数据失败");
		}
		List<NewShopTask> finished = new ArrayList<>();
		List<NewShopTask> unfinished = new ArrayList<>();
		taskList.forEach(newShopTask -> {
			int status = newShopTask.getStatus();
			if (status == NewShopTaskEnum.task_finished.getCode()) {
				finished.add(newShopTask);
			} else {
				unfinished.add(newShopTask);
			}
		});
		// 排序+赋值
		this.setValue(unfinished, taskMap, taskVos);
		taskVos.sort(TaskVo::compareTo);
		this.setValue(finished, taskMap, taskVos);
		resultVO.setT(taskVos);
		return resultVO.format(true, "成功", taskVos);
	}

	/**
	 * 领取注册奖励
	 * @param user
	 */
	@Override
	public ResultVO<Integer> registeredRewards(UserDto user, String trackId) {
		return this.finishTask(user.getShopId(), NewShopEnum.registered, new Date(), trackId);
	}

	/**
	 * 判断是否符合任务条件
	 *
	 * @param shopId
	 * @param shopTime
	 * @param trackId
	 * @return
	 */
	@Override
	public boolean isShowNewShopTask(Long shopId, Date shopTime, String trackId) {
		String begEndTime = this.getHotConf(NewShopEnum.welfareTimeKey, trackId);
		if (StringUtils.isEmpty(begEndTime)) {
			logger.info("active have not begin,shopId{},trackId{}", shopId, trackId);
			return false;
		}
		String[] split = begEndTime.split("~");
		Date begTime = DateTimeTool.getDateFromString(split[0]);
		Date endTime = DateTimeTool.getDateFromString(split[1]);
		Date currentTime = new Date();
		if (currentTime.before(begTime) || currentTime.after(endTime)) {
			logger.info("active invalid,shopId{},trackId{}", shopId, trackId);
			return false;
		}
		Date invalidTime = SmartDateUtil.getAddDayBeginTime(30, shopTime);
		if (invalidTime.before(currentTime)) {
			return false;
		}
		return true;
	}

	// 完成任务
	@Override
	public ResultVO<Integer> finishTask(Long shopId, String taskCode, Date time, String trackId) {
		ResultVO<Integer> resultVO = new ResultVO<>();
		Jedis jedis = null;
		try {
			jedis = RedisHelper.getJedis();

			// 更新任务列表
			Map<String, TaskVo> hotConfigTaskInfo = this.getHotConfigTaskInfo(time, trackId);
			TaskVo taskVo = hotConfigTaskInfo.get(taskCode);
			Integer medalNum = taskVo.getMedalNum(); //当前任务奖章数
			NewShopTask newShopTask = new NewShopTask();
			newShopTask.setShopId(shopId);
			newShopTask.setTaskCode(taskCode);
			newShopTask.setMedalNum(medalNum);
			newShopTask.setModifyDate(new Date());
			newShopTask.setFinishedTime(new Date());
			newShopTask.setTaskUrl(taskVo.getJumpUrl());
			newShopTask.setTaskDescUp(taskVo.getTaskDescUp());
			newShopTask.setTaskDescDown(taskVo.getTaskDescDown());
			newShopTask.setStatus(NewShopTaskEnum.task_finished.getCode());
			int num = newShopTaskDao.updateTask(newShopTask);

			// 更新福利基本信息
			NewShopWelfare welfare = this.getWelfareByShopId(shopId, jedis, trackId);
			int welfareStatus = welfare.getWelfareStatus();
			int newMedalNum = taskVo.getMedalNum() + welfare.getMedalNum();
			welfare.setMedalNum(newMedalNum);
			if (newMedalNum >= 10 && welfareStatus != NewShopTaskEnum.medal_obtain.getCode()) {
				welfare.setWelfareStatus(NewShopTaskEnum.medal_obtain.getCode());
				String code = this.getHotConf(NewShopEnum.couponCode, trackId);
				String quantity = this.getHotConf(NewShopEnum.couponQuantity, trackId);
				sendCouponService.sendCouponToNewShop(welfare.getUserId(), code, Integer.valueOf(quantity));
			}
			int medal = newShopWelfareDao.updateWelfare(welfare);
			if (num == 0 || medal == 0) {
				logger.error("领取奖章失败 shopId{},trackId{}", shopId, trackId);
				throw new Exception("领取奖章更新数据库失败trackId" + trackId);
			}

			// 刷新缓存
			this.freshWelfareCache(jedis, welfare, trackId);
			this.freshTaskCache(shopId, jedis);
		} catch (Exception e) {
			logger.error("error,trackId" + trackId, e);
			return resultVO.format(false, "领取奖章失败");
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		resultVO.setT(1);
		return resultVO.format(true, "success");
	}

	@Override
	@Transactional
	public List<NewShopTask> getTasks(Long userId, Long shopId, Map<String, TaskVo> taskMap, String trackId) {
		List<NewShopTask> tasks = new ArrayList<>();
		String value = "";
		try (Jedis jedis = RedisHelper.getJedis()) {
			String key = NewShopEnum.cacheBaseKey + NewShopEnum.taskList + shopId;
			value = jedis.get(key);
			if (StringUtils.isNotEmpty(value)) {
				tasks = JSONObject.parseArray(value, NewShopTask.class);
			}
			if (StringUtils.isEmpty(value)) {
				logger.info("no cache shopId{},trackId{}", shopId, trackId);
				tasks = this.freshTaskCache(shopId, jedis);
			}
			if (StringUtils.isEmpty(value)) {
				for (Map.Entry<String, TaskVo> entry : taskMap.entrySet()) {
					String taskCode = entry.getKey();
					NewShopTask newShopTask = new NewShopTask();
					newShopTask.setUserId(userId);
					newShopTask.setShopId(shopId);
					newShopTask.setTaskCode(taskCode);
					newShopTask.setCreateDate(new Date());
					newShopTask.setModifyDate(new Date());
					newShopTask.setStatus(NewShopTaskEnum.task_unfinished.getCode());
					if (NewShopEnum.registered.equals(taskCode)) {
						newShopTask.setStatus(NewShopTaskEnum.task_receive.getCode());
					}
					newShopTaskDao.insertTask(newShopTask);
				}
				tasks = this.freshTaskCache(shopId, jedis);
			}
		}
		return tasks;
	}

	// 获取热配的任务信息
	@Override
	public Map<String, TaskVo> getHotConfigTaskInfo(Date time, String trackId) {
		Map<String, TaskVo> taskVoMap = new HashMap<>();
		String taskListStr = this.getHotConf(NewShopEnum.hotTaskList, trackId);
		JSONObject jsonObject = JSONObject.parseObject(taskListStr);
		Set<String> keys = jsonObject.keySet();
		for (String key : keys) {
			String value = jsonObject.getString(key);
			List<TaskVo> taskVos = JSONObject.parseArray(value, TaskVo.class);
			for (TaskVo taskVo : taskVos) {
				if (null == taskVo.getEndTime()) {
					taskVoMap.put(key, taskVo);
					break;
				}
				if (time.after(taskVo.getBegTime()) && time.before(taskVo.getEndTime())) {
					taskVoMap.put(key, taskVo);
					break;
				}
			}
		}
		return taskVoMap;
	}

	/**
	 * 获取当前福利信息
	 *
	 * @param shopId
	 * @param jedis
	 * @param trackId
	 * @return
	 * @throws Exception
	 */
	@Override
	public NewShopWelfare getWelfareByShopId(long shopId, Jedis jedis, String trackId) throws Exception {
		NewShopWelfare welfare = null;
		String key = NewShopEnum.cacheBaseKey + NewShopEnum.welfareBase + shopId;
		String value = jedis.get(key);
		welfare = JSONObject.parseObject(value, NewShopWelfare.class);
		if (null == welfare) {
			welfare = newShopWelfareDao.getWelfare(shopId);
		}
		if (null != welfare) {
			this.freshWelfareCache(jedis, welfare, trackId);
		}
		if (null == welfare) {
			Shop shop = UserShopClient.getShop(shopId);
			boolean b = this.initNewShopTask(shop.getOwnerId(), shop, jedis, trackId);
			if (b) {
				String s = jedis.get(key);
				welfare = JSONObject.parseObject(s, NewShopWelfare.class);
			}
		}
		return welfare;
	}

	// 刷新福利缓存
	public boolean freshWelfareCacheWithJedis(NewShopWelfare welfare, String trackId) {
		try (Jedis jedis = RedisHelper.getJedis()) {
			return this.freshWelfareCache(jedis, welfare, trackId);
		}
	}
	/**
	 * 初始化新店主福利信息 + 任务列表 + 缓存
	 *
	 * @param userId
	 * @param shop
	 * @param trackId
	 */
	private boolean initNewShopTask(Long userId, Shop shop, Jedis jedis, String trackId) {
		logger.info("用户首次进入店铺shopId{},trackId{}", shop.getId(), trackId);
		NewShopWelfare welfare = new NewShopWelfare();
		Date date = new Date();
		welfare.setMedalNum(0);
		welfare.setCreateDate(date);
		welfare.setModifyDate(date);
		welfare.setUserId(userId);
		welfare.setShopId(shop.getId());
		welfare.setShopTime(shop.getCreatedDate());
		welfare.setStatus(NewShopTaskEnum.welfare_valid.getCode());
		welfare.setPrizeOne(NewShopTaskEnum.first_order_init.getCode());
		welfare.setPrizeTwo(NewShopTaskEnum.first_order_init.getCode());
		welfare.setWelfareStatus(NewShopTaskEnum.medal_no_get.getCode());
		long id = newShopWelfareDao.initWelfare(welfare);
		welfare.setId(id);
		if (id == 0) {
			return false;
		}
		this.freshWelfareCache(jedis, welfare, trackId);
		return true;
	}

	// 根据 shopId 获取福利信息
	private NewShopWelfare getWelfare(UserDto user, String trackId) throws Exception {
		if (null == user.getShopId())
			logger.info("店铺id为空，user{},trackId{}", user.getNickName(), trackId);
		NewShopWelfare welfare = null;
		try (Jedis jedis = RedisHelper.getJedis()) {
			welfare = this.getWelfareByShopId(user.getShopId(), jedis, trackId);
		}
		return welfare;
	}

	// 刷新任务缓存
	private List<NewShopTask> freshTaskCache(Long shopId, Jedis jedis) {
		String key = NewShopEnum.cacheBaseKey + NewShopEnum.taskList + shopId;
		List<NewShopTask> tasks = newShopTaskDao.queryNewShopTask(shopId);
		if (null == tasks || tasks.size() == 0) {
			return null;
		}
		String value = JSONObject.toJSONString(tasks);
		jedis.setex(key, ex, value);
		return tasks;
	}

	// 刷新福利缓存
	private boolean freshWelfareCache(Jedis jedis, NewShopWelfare welfare, String trackId) {
		String key = NewShopEnum.cacheBaseKey + NewShopEnum.welfareBase + welfare.getShopId();
		String value = JSONObject.toJSONString(welfare);
		String setex = jedis.setex(key, ex, value);
		if ("ok".equals(setex)) {
			return true;
		}
		logger.info("freshWelfareCache error value{},trackId{}", value, trackId);
		return false;
	}

	// 开单奖励
	private List<FirstOrderVo> getFirstOrderVo(NewShopWelfare welfare, NewShopModuleVO result, String trackId) {
		List<FirstOrderVo> firstOrderVos = new ArrayList<>();
		Date shopTime = welfare.getShopTime();
		String firstOrderHotValue = this.getHotConf(NewShopEnum.firstOrderHotKey, trackId);
		List<FirstOrderVo> hotConf = JSONObject.parseArray(firstOrderHotValue, FirstOrderVo.class);
		FirstOrderVo one = hotConf.get(0);
		FirstOrderVo two = hotConf.get(1);

		Date addOneDay = SmartDateUtil.getAddDayBeginTime(1, shopTime);
		Date addTwoDay = SmartDateUtil.getAddDayBeginTime(2, shopTime);
		Date addForDay = SmartDateUtil.getAddDayBeginTime(4, shopTime);
		Date addFivDay = SmartDateUtil.getAddDayBeginTime(5, shopTime);

		if (welfare.getPrizeOne() == NewShopTaskEnum.first_order_obtain.getCode()) {
			one.setStatus(welfare.getPrizeOne());
			one.setDeadline(addOneDay);
			firstOrderVos.add(one);
			return firstOrderVos;
		}
		if (welfare.getPrizeTwo() == NewShopTaskEnum.first_order_obtain.getCode()) {
			one.setStatus(welfare.getPrizeOne());
			one.setDeadline(addOneDay);
			two.setStatus(welfare.getPrizeOne());
			two.setDeadline(addForDay);
			firstOrderVos.add(one);
			firstOrderVos.add(two);
			return firstOrderVos;
		}
		if (welfare.getPrizeOne() == NewShopTaskEnum.first_order_overdue.getCode()) {
			one.setStatus(welfare.getPrizeOne());
			one.setDeadline(addOneDay);
			firstOrderVos.add(one);
		}
		if (welfare.getPrizeTwo() == NewShopTaskEnum.first_order_overdue.getCode()) {
			two.setStatus(welfare.getPrizeOne());
			two.setDeadline(addForDay);
			firstOrderVos.add(two);
			return firstOrderVos;
		}
		if (welfare.getPrizeTwo() == NewShopTaskEnum.first_order_init.getCode()) {
			Date current = new Date();
			if (current.after(addFivDay)) {
				one.setDeadline(addOneDay);
				two.setDeadline(addForDay);
				one.setStatus(NewShopTaskEnum.first_order_overdue.getCode());
				two.setStatus( NewShopTaskEnum.first_order_overdue.getCode());
				firstOrderVos.add(one);
				firstOrderVos.add(two);
				welfare.setModifyDate(new Date());
				welfare.setPrizeOne(NewShopTaskEnum.first_order_overdue.getCode());
				welfare.setPrizeTwo(NewShopTaskEnum.first_order_overdue.getCode());
				newShopWelfareDao.updateWelfare(welfare);
				this.freshWelfareCacheWithJedis(welfare,trackId);
			} else if (current.after(addTwoDay)) {
				one.setDeadline(addOneDay);
				one.setStatus(NewShopTaskEnum.first_order_overdue.getCode());
				two.setDeadline(addForDay);
				two.setStatus( NewShopTaskEnum.first_order_init.getCode());
				firstOrderVos.add(one);
				firstOrderVos.add(two);
				welfare.setPrizeOne(NewShopTaskEnum.first_order_overdue.getCode());
				welfare.setModifyDate(new Date());
				newShopWelfareDao.updateWelfare(welfare);
				this.freshWelfareCacheWithJedis(welfare,trackId);
			} else {
				one.setDeadline(addOneDay);
				one.setStatus(NewShopTaskEnum.first_order_init.getCode());
				firstOrderVos.add(one);
			}
			result.setPartMove(1);
		}
		return firstOrderVos;
	}

	private String getHotConf(String key, String trackId) {
		String value = CommonConstant.getHotConfigValue(key, "").trim();
		if (StringUtils.isEmpty(value)) {
			logger.info("获取热配key{}为空trackId{}", key, trackId);
			value = NewShopTaskTest.getStr(trackId,key);// todo whm 自测使用合并删除
		}
		return value;
	}

	private void setValue(List<NewShopTask> tasks, Map<String, TaskVo> taskMap, List<TaskVo> taskVos) {
		tasks.forEach(task -> {
			TaskVo taskVo = taskMap.get(task.getTaskCode());
			if (null != taskVo) {
				taskVo.setStatus(task.getStatus());
				if (task.getStatus() == NewShopTaskEnum.task_finished.getCode()) {
					taskVo.setButtonPrompt(NewShopEnum.buttonPrompt);
				}
				taskVo.setMedalNum(task.getMedalNum());
			} else {
				taskVo.setStatus(NewShopTaskEnum.task_unfinished.getCode());
			}
			taskVos.add(taskVo);
		});
	}
}
