package com.daling.party.controller.newShop;

import com.daling.party.controller.dto.UserDto;
import com.daling.party.domain.vo.newShop.NewShopEntranceVO;
import com.daling.party.domain.vo.newShop.NewShopModuleVO;
import com.daling.party.domain.vo.newShop.childVo.TaskVo;
import com.daling.party.infrastructure.base.BaseController;
import com.daling.party.infrastructure.model.ResultVO;
import com.daling.party.infrastructure.utils.GenUserUtil;
import com.daling.party.infrastructure.utils.XcHeadWrapper;
import com.daling.party.service.newShop.INewShopTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

/**
 * 新店主引导任务
 */
@RequestMapping("newShop/task")
@Controller
@Slf4j
public class NewShopTaskController extends BaseController {

	@Autowired
	private INewShopTaskService newShopTaskService;

	/**
	 * 是否展示新店主福利入口
	 */
	@RequestMapping("entrance.do")
	@ResponseBody
	XcHeadWrapper<NewShopEntranceVO> isShowEntrance(UserDto user, String trackId) {
		trackId = StringUtils.isBlank(trackId) ? UUID.randomUUID().toString() : trackId;
		XcHeadWrapper<NewShopEntranceVO> jsonWrapper = initJsonHeaderWrapper(trackId);
		try {
			user = GenUserUtil.gen(trackId,user);// todo whm 本地测试使用 合并代码删除
			ResultVO<NewShopEntranceVO> resultVO = newShopTaskService.isShowEntrance(user, trackId);
			this.getResult(jsonWrapper, resultVO);
		} catch (Exception e) {
			log.error("error", e);
			jsonWrapper.setStatus(XcHeadWrapper.StatusEnum.Failed.getCode());
			jsonWrapper.setErrorMsg(e.getMessage());
		} finally {
			jsonWrapper.timeWatchStop();
		}
		return jsonWrapper;
	}

	/**
	 * ABC模块信息
	 */
	@RequestMapping("index.do")
	@ResponseBody
	XcHeadWrapper<NewShopModuleVO> index(UserDto user, String trackId) {
		trackId = StringUtils.isBlank(trackId) ? UUID.randomUUID().toString() : trackId;
		XcHeadWrapper<NewShopModuleVO> jsonWrapper = initJsonHeaderWrapper(trackId);
		try {
			user = GenUserUtil.gen(trackId,user);// todo whm 本地测试使用 合并代码删除
			ResultVO<NewShopModuleVO> resultVO = newShopTaskService.getModuleInfo(user, trackId);
			this.getResult(jsonWrapper, resultVO);
		} catch (Exception e) {
			log.error("error", e);
			jsonWrapper.setStatus(XcHeadWrapper.StatusEnum.Failed.getCode());
			jsonWrapper.setErrorMsg(e.getMessage());
		} finally {
			jsonWrapper.timeWatchStop();
		}
		return jsonWrapper;
	}

	/**
	 * 任务列表
	 */
	@RequestMapping("tasks.do")
	@ResponseBody
	XcHeadWrapper<List<TaskVo>> getTasks(UserDto user, String trackId) {
		trackId = StringUtils.isBlank(trackId) ? UUID.randomUUID().toString() : trackId;
		XcHeadWrapper<List<TaskVo>> jsonWrapper = initJsonHeaderWrapper(trackId);
		try {
			user = GenUserUtil.gen(trackId,user);// todo whm 本地测试使用 合并代码删除
			ResultVO<List<TaskVo>> resultVO = newShopTaskService.getTaskList(user, trackId);
			this.getResult(jsonWrapper, resultVO);
		} catch (Exception e) {
			log.error("error", e);
			jsonWrapper.setStatus(XcHeadWrapper.StatusEnum.Failed.getCode());
			jsonWrapper.setErrorMsg(e.getMessage());
		} finally {
			jsonWrapper.timeWatchStop();
		}
		return jsonWrapper;
	}

	/**
	 * 注册领取奖章接口
	 */
	@RequestMapping("registeredRewards.do")
	@ResponseBody
	XcHeadWrapper<Integer> registeredRewards(UserDto user, String trackId) {
		trackId = StringUtils.isBlank(trackId) ? UUID.randomUUID().toString() : trackId;
		XcHeadWrapper<Integer> jsonWrapper = initJsonHeaderWrapper(trackId);
		try {
			user = GenUserUtil.gen(trackId,user);// todo whm 本地测试使用 合并代码删除
			ResultVO<Integer> resultVO = newShopTaskService.registeredRewards(user, trackId);
			this.getResult(jsonWrapper, resultVO);
		} catch (Exception e) {
			log.error("error", e);
			jsonWrapper.setStatus(XcHeadWrapper.StatusEnum.Failed.getCode());
			jsonWrapper.setErrorMsg(e.getMessage());
		} finally {
			jsonWrapper.timeWatchStop();
		}
		return jsonWrapper;
	}

	private void getResult(XcHeadWrapper jsonWrapper, ResultVO resultVO) {
		jsonWrapper.setData(resultVO.getT());
		jsonWrapper.setErrorMsg(resultVO.getMessage());
		jsonWrapper.setStatus(resultVO.isRetBool() ? XcHeadWrapper.StatusEnum.Success.getCode() : XcHeadWrapper.StatusEnum.Failed.getCode());
	}
}
