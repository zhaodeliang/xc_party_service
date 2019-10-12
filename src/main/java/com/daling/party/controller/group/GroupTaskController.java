package com.daling.party.controller.group;

import com.daling.common.dmonitor.DMonitor;
import com.daling.common.jsonwrapper.JsonHeaderWrapper;
import com.daling.party.infrastructure.base.BaseController;
import com.daling.party.infrastructure.model.ResultVO;
import com.daling.party.infrastructure.utils.WebUtil;
import com.daling.party.infrastructure.utils.XcHeadWrapper;
import com.daling.party.service.group.GroupTaskService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("group/task")
@Slf4j
public class GroupTaskController extends BaseController{
    @Autowired
    private GroupTaskService groupTaskService;

    /**
     * 定时任务-即将结束的拼团推送消息
     * @return
     */
    @RequestMapping("sendRemainingMessage.do")
    public JsonHeaderWrapper sendRemainingMessage() {
        JsonHeaderWrapper<ResultVO<String>> result = new JsonHeaderWrapper<>();
        ResultVO<String> resultVO = new ResultVO<>();
        try {
            resultVO = groupTaskService.sendRemainingMessage();
        } catch (Exception e) {
            log.error("sendRemainingMessage error", e);
            resultVO.format(false, e.getMessage());
        } finally {
            result.timeWatchStop();
        }
        result.setGet_url(WebUtil.getRequestURI(request));
        result.setStatus(resultVO.isRetBool() ? 0 : 1);
        result.setData(resultVO);
        DMonitor.recordOne("apiTask_sendRemainingMessage", result.timeWatchStop());
        return result;
    }

    /**
     * 处理发货、退款任务
     * @return
     */
    @RequestMapping(value = "groupValidMemberTask.do")
    public JsonHeaderWrapper handlerValidGroupMemberTask() {
        JsonHeaderWrapper jsonHeaderWrapper = new JsonHeaderWrapper();
        ResultVO<String> resultVO = new ResultVO<>();
        try {
            resultVO = groupTaskService.handlerValidGroupMember();
        } catch (Exception e) {
            log.error("groupValidMemberTask error", e);
            resultVO.format(false, e.getMessage());
        }
        jsonHeaderWrapper.setStatus(resultVO.isRetBool() ? 0 : 1);
        jsonHeaderWrapper.setGet_url(WebUtil.getRequestURI(request));
        jsonHeaderWrapper.setData(resultVO);
        DMonitor.recordOne("apiTask_groupValidMemberTask", jsonHeaderWrapper.timeWatchStop());
        return jsonHeaderWrapper;
    }
}
