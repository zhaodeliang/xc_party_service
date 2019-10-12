package com.daling.party.controller.group;

import com.alibaba.fastjson.JSON;
import com.daling.common.dmonitor.DMonitor;
import com.daling.party.controller.group.converter.GroupConverter;
import com.daling.party.controller.group.converter.GroupMsgConverter;
import com.daling.party.controller.group.req.DraftGroupRequest;
import com.daling.party.controller.group.req.JoinGroupRequest;
import com.daling.party.controller.group.req.ShareGroupRequest;
import com.daling.party.controller.group.resp.GroupDetailResp;
import com.daling.party.controller.group.resp.GroupJoinStatusResp;
import com.daling.party.controller.group.resp.GroupMsgResp;
import com.daling.party.infrastructure.base.BaseController;
import com.daling.party.infrastructure.exception.GenericBusinessException;
import com.daling.party.infrastructure.model.ResultVO;
import com.daling.party.infrastructure.utils.XcHeadWrapper;
import com.daling.party.service.group.GroupService;
import com.daling.party.service.group.bo.GroupBo;
import com.daling.party.service.group.bo.GroupMsgBo;
import com.daling.party.service.group.to.ShareGroupTo;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("group/unCheck")
@Slf4j
public class GroupUnCheckController extends BaseController {
    @Autowired
    private GroupService groupService;

    // 根据团编码活动查询团信息
    @GetMapping(value = "loadGroupMessage.do")
    public ResultVO<GroupMsgResp> loadGroupMessage(@RequestParam String groupCode, String orderNo) {

        ResultVO<GroupMsgResp> resultVO = new ResultVO<>();
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();

            GroupMsgBo groupMsgBo = groupService.loadGroupMessage(groupCode, orderNo);
            log.info("loadGroupMessage groupCode: {}, orderNo: {}, response {}", groupCode, orderNo, JSON.toJSONString(groupMsgBo));
            DMonitor.recordOne("apiUn_loadGroupMessage", stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return resultVO.format(true, "查询成功", GroupMsgConverter.bo2Resp(groupMsgBo));
        } catch (Exception e) {
            if (e instanceof GenericBusinessException) {
                return resultVO.format(false, e.getMessage());
            }
            log.error("根据团编码活动查询团信息异常，groupCode:{}", groupCode, e);
            return resultVO.format(false, "根据团编码活动查询团信息异常");
        }
    }

    /**
     * 根据团编码查询团活动信息
     *
     * @param groupCode
     * @return
     */
    @GetMapping(value = "loadGroupMessageByCode.do")
    public ResultVO<GroupDetailResp.Group> loadGroupMessageByCode(@RequestParam String groupCode) {
        ResultVO<GroupDetailResp.Group> resultVO = new ResultVO<>();
        Stopwatch stopwatch = Stopwatch.createStarted();

        GroupDetailResp.Group group;
        try {
            group = GroupMsgConverter.bo2Resp(groupService.getGroupByCode(groupCode));
        } catch (Exception e) {
            log.error("loadGroupMessageByCode.do is error" + e.getMessage(), e);
            return resultVO.format(false, e.getMessage(), XcHeadWrapper.StatusEnum.UnknownOther.getCode());
        }
        DMonitor.recordOne("apiUn_loadGroupMessageByCode", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return resultVO.format(true, "查询成功", group);
    }

    /**
     * 正在拼团个数
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "myCurrentGroupCount.do")
    public ResultVO<Integer> myCurrentGroupCount(Long userId) {
        ResultVO<Integer> resultVO = new ResultVO<>();
        Stopwatch stopwatch = Stopwatch.createStarted();
        Integer integer = null;
        try {
            integer = groupService.userGroupCount(userId);
        } catch (Exception e) {
            log.error("myCurrentGroupCount error message={}" + e.getMessage(), e);
            resultVO.format(false, e.getMessage(), XcHeadWrapper.StatusEnum.UnknownOther.getCode());
        }
        DMonitor.recordOne("apiUn_myCurrentGroupCount", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return resultVO.format(true, "查询成功", integer);
    }

    /**
     * 清除指定缓存
     *
     * @param redisKey
     * @return
     */
    @GetMapping(value = "clearRedisCash.do")
    public ResultVO<Long> clearRedisCash(@RequestParam String redisKey) {
        ResultVO<Long> resultVO = new ResultVO<>();
        Stopwatch stopwatch = Stopwatch.createStarted();
        Long aLong = null;
        try {
            aLong = groupService.clearRedisCash(redisKey);
        } catch (Exception e) {
            log.error("clearRedisCash error message={}" + e.getMessage(), e);
            resultVO.format(false, e.getMessage(), XcHeadWrapper.StatusEnum.UnknownOther.getCode());
        }
        DMonitor.recordOne("apiUn_clearRedisCash", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return resultVO.format(true, "删除key成功", aLong);
    }

    /**
     * 根据团编码获取团的加入状态
     *
     * @param groupCode
     * @param userId
     * @return
     */
    @GetMapping(value = "loadGroupJoinStatus.do")
    public ResultVO<GroupJoinStatusResp> loadGroupJoinStatus(@RequestParam String groupCode, @RequestParam Long userId) {
        ResultVO<GroupJoinStatusResp> resultVO = new ResultVO<>();
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();

            GroupJoinStatusResp joinStatus = groupService.loadGroupJoinStatus(groupCode, userId);
            log.info("loadGroupJoinStatus groupCode: {}, userId: {}, response: {}", groupCode, userId, JSON.toJSONString(joinStatus));
            DMonitor.recordOne("apiUn_loadGroupJoinStatus", stopwatch.elapsed(TimeUnit.MILLISECONDS));
            return resultVO.format(true, "查询成功", joinStatus);
        } catch (Exception e) {
            log.error("根据团编码获取团的加入状态异常，groupCode:{}", groupCode, e);
            return resultVO.format(false, "根据团编码获取团的加入状态异常");
        }
    }

    /**
     * 自购类型 - 创建草稿团
     *
     * @param draftGroupRequest
     * @return
     */
    @RequestMapping(value = "createDraftGroup.do", method = RequestMethod.POST)
    public ResultVO<String> createDraftGroup(@RequestBody @Valid DraftGroupRequest draftGroupRequest) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ResultVO<String> draftGroup = groupService.createDraftGroup(draftGroupRequest.getHeadId(), draftGroupRequest.getActivityCode(), draftGroupRequest.getSpu());
        DMonitor.recordOne("apiUn_create_draft_group", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return draftGroup;
    }

    /**
     * 创建分享团
     *
     * @param shareGroupRequest
     * @return
     */
    @RequestMapping(value = "createShareGroup.do", method = RequestMethod.POST)
    public ResultVO<ShareGroupTo> createShareGroup(@RequestBody @Valid ShareGroupRequest shareGroupRequest) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ResultVO<ShareGroupTo> shareGroup = groupService.createShareGroup(GroupConverter.req2Bo(shareGroupRequest));
        DMonitor.recordOne("apiUn_create_share_group", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return shareGroup;
    }

    /**
     * 参团接口  用于压测
     * @param joinGroupRequest
     */
    @RequestMapping(value = "joinGroup.do", method = RequestMethod.POST)
    public void joinGroup(@RequestBody @Valid JoinGroupRequest joinGroupRequest) {
        GroupBo groupBo = GroupConverter.req2Bo(joinGroupRequest);
        try {
            groupService.joinGroup(groupBo);
        } catch (Exception e) {
            log.error("参团失败", e);
        }
    }
}
