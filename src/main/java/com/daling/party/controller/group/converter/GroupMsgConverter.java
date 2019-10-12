package com.daling.party.controller.group.converter;

import com.daling.party.controller.group.resp.GroupDetailResp;
import com.daling.party.controller.group.resp.GroupListGoodsDetailResp;
import com.daling.party.controller.group.resp.GroupMsgResp;
import com.daling.party.domain.entity.Group;
import com.daling.party.infrastructure.utils.AssertUtil;
import com.daling.party.infrastructure.utils.DateTimeTool;
import com.daling.party.infrastructure.utils.SystemUtil;
import com.daling.party.service.group.bo.GroupMsgBo;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by lilindong on 2019/4/8.
 */
public class GroupMsgConverter {

    private GroupMsgConverter() {}

    /**
     * 团信息回参数
     * @param groupMsgBo
     * @return
     */
    public static GroupMsgResp bo2Resp(GroupMsgBo groupMsgBo) {
        AssertUtil.notNull(groupMsgBo, "bo is null");

        Integer status = SystemUtil.judgeGroupTrueStatus(System.currentTimeMillis(),
                groupMsgBo.getEndTime().getTime(), groupMsgBo.getSurplusNum());
        GroupMsgResp resp = GroupMsgResp.builder()
                .groupCode(groupMsgBo.getGroupCode()).status(status)
                .groupType(groupMsgBo.getGroupType()).groupRule(groupMsgBo.getGroupRule())
                .upperLimit(groupMsgBo.getUpperLimit()).lowerLimit(groupMsgBo.getLowerLimit())
                .surplusNum(groupMsgBo.getSurplusNum()).joinNum(groupMsgBo.getJoinNum())
                .startTime(groupMsgBo.getStartTime()).endTime(groupMsgBo.getEndTime())
                .headUserId(groupMsgBo.getHeadUserId()).headImg(groupMsgBo.getHeadImg())
                .headNickname(groupMsgBo.getHeadNickname()).createTime(groupMsgBo.getCreateTime())
                .isJoin(groupMsgBo.getIsJoin())
                .build();
        // 拼团剩余时间
        if (groupMsgBo.getEndTime() != null && groupMsgBo.getEndTime().getTime() > System.currentTimeMillis()) {
            resp.setSurplusTime(groupMsgBo.getEndTime().getTime() - System.currentTimeMillis());
        }
        return resp;
    }

    /**
     * 团信息回参数
     * @param group
     * @return
     */
    public static GroupDetailResp.Group bo2Resp(Group group) {
        AssertUtil.notNull(group, "group is null");

        GroupDetailResp.Group minGroup = GroupDetailResp.Group.builder().groupType(group.getGroupType()).upperLimit(group.getUpperLimit())
                .lowerLimit(group.getLowerLimit()).groupEndDate(DateTimeTool.CSTFormat(group.getEndTime().toString()))
                .joinNum(group.getJoinNum()).build();
        return minGroup;
    }

    public static List<GroupMsgResp> bo2Resp(List<GroupMsgBo> boList) {
        List<GroupMsgResp> respList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(boList)) {
            boList.forEach(item -> respList.add(GroupMsgConverter.bo2Resp(item)));
        }
        return respList;
    }

    /**
     * 商品详情页面回参
     * @param groupMsgBo
     * @return
     */
    public static GroupListGoodsDetailResp bo2GoodsDetailResp(GroupMsgBo groupMsgBo) {
        AssertUtil.notNull(groupMsgBo, "bo is null");

        return GroupListGoodsDetailResp.builder()
                .groupCode(groupMsgBo.getGroupCode()).surplusNum(groupMsgBo.getSurplusNum())
                .headUserId(groupMsgBo.getHeadUserId()).headImg(groupMsgBo.getHeadImg())
                .headNickname(groupMsgBo.getHeadNickname())
                .build();
    }

    public static List<GroupListGoodsDetailResp> bo2GoodsDetailResp(List<GroupMsgBo> boList) {
        List<GroupListGoodsDetailResp> respList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(boList)) {
            boList.forEach(item -> respList.add(GroupMsgConverter.bo2GoodsDetailResp(item)));
        }
        return respList;
    }
}
