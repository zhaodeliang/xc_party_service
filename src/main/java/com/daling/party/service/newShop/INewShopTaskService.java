package com.daling.party.service.newShop;

import com.daling.party.controller.dto.UserDto;
import com.daling.party.domain.entity.NewShopTask;
import com.daling.party.domain.entity.NewShopWelfare;
import com.daling.party.domain.vo.newShop.NewShopEntranceVO;
import com.daling.party.domain.vo.newShop.NewShopModuleVO;
import com.daling.party.domain.vo.newShop.childVo.TaskVo;
import com.daling.party.infrastructure.model.ResultVO;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface INewShopTaskService {

	/**
	 * 是否展示新店主福利入口
	 * @param user
	 * @param trackId
	 * @return
	 */
	ResultVO<NewShopEntranceVO> isShowEntrance(UserDto user, String trackId);

	/**
	 * 获取ABCD模块信息
	 * @param user
	 * @param trackId
	 * @return
	 */
	ResultVO<NewShopModuleVO> getModuleInfo(UserDto user, String trackId) throws Exception;

	/**
	 * 获取任务列表
	 * @param user
	 * @param trackId
	 * @return
	 */
	ResultVO<List<TaskVo>> getTaskList(UserDto user, String trackId);

	/**
	 * 领取注册奖励
	 * @param user
	 * @param trackId
	 * @return
	 */
	ResultVO<Integer> registeredRewards(UserDto user, String trackId);

	/**
	 * 判断是否符合新店主任务标椎
	 * @param shopId
	 * @param trackId
	 * @return
	 */
	boolean isShowNewShopTask(Long shopId, Date shopTime, String trackId);

	/**
	 * 获取任务列表
	 * @param shopId
	 * @param trackId
	 * @return
	 */
	List<NewShopTask> getTasks(Long shopId, Long userId, Map<String, TaskVo> taskMap, String trackId);

	/**
	 * 获取任务热配信息
	 * @param trackId
	 * @return
	 */
	Map<String, TaskVo> getHotConfigTaskInfo(Date time, String trackId);

	/**
	 * 完成任务领取奖章
	 * @param shopId
	 * @param taskCode
	 * @param trackId
	 * @return
	 */
	ResultVO<Integer> finishTask(Long shopId, String taskCode, Date time, String trackId);

	/**
	 * 获取精选信息
	 * @param shopId
	 * @param jedis
	 * @param trackId
	 * @return
	 * @throws Exception
	 */
	NewShopWelfare getWelfareByShopId(long shopId, Jedis jedis, String trackId) throws Exception;

	/**
	 * 刷新福利缓存
	 * @param welfare
	 * @param trackId
	 * @return
	 */
	boolean freshWelfareCacheWithJedis(NewShopWelfare welfare, String trackId);
}
