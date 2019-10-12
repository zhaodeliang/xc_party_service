package com.daling.party.service.group.impl;

import com.daling.party.domain.entity.Group;
import com.daling.party.domain.entity.GroupMember;
import com.daling.party.domain.group.enums.GroupOrderStatusEnum;
import com.daling.party.domain.group.enums.GroupRoleEnum;
import com.daling.party.infrastructure.enums.MessageEnum;
import com.daling.party.infrastructure.model.ResultVO;
import com.daling.party.repository.cache.GroupCache;
import com.daling.party.repository.cache.GroupMemberCache;
import com.daling.party.repository.dao.GroupDao;
import com.daling.party.repository.dao.GroupMemberDao;
import com.daling.party.service.group.bo.GroupBo;
import com.daling.party.service.group.bo.ValidBo;
import com.daling.party.service.group.send.GroupSend;
import com.daling.party.service.group.to.ShareGroupTo;
import com.daling.ucclient.enums.YesNoEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testng.collections.Lists;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author jiwei.xue
 * @date 2019/5/10 10:23
 */
@Service
@Slf4j
public class GroupTranServiceImpl {

    @Resource
    private GroupDao groupDao;

    @Resource
    private GroupMemberDao groupMemberDao;

    @Resource
    private GroupSend groupSend;

    @Resource
    private GroupCache groupCache;

    @Resource
    private GroupMemberCache groupMemberCache;

    @Transactional(rollbackFor = Exception.class)
    public void joinGroup(GroupMember groupMember, Boolean groupSuccess, List<Long> userIds, GroupBo groupBo, Group group, List<GroupMember> groupMembers) {
        //修改Group
        groupDao.updateByPrimaryKey(group);
        //插入GroupMember
        groupMemberDao.insertSelective(groupMember);
        if (groupSuccess) {
            //批量修改订单状态
            userIds.add(groupBo.getUserId());
            groupMemberDao.updateOrderStatus(GroupOrderStatusEnum.WAIT_DELIVER.getCode(), MessageEnum.invalid_flag.getCode(), userIds, group.getId());
            //批量发送消息
            groupMembers.add(groupMember);
            groupMembers.forEach(member -> {
                groupSend.sendValidGroupMsgToMq(buildValidBo(member));
            });
        }
        log.info("参团成功");
        //删除缓存
        groupCache.deleteGroup(groupBo.getGroupCode());
        groupMemberCache.deleteMemberList(group.getId(), YesNoEnum.YES.getCode());
        //刷新用户正在拼团的个数
        groupMemberCache.delCurrentGroupList(groupBo.getUserId());
        if (!Objects.equals(groupBo.getUserId(), group.getHeadId())) {
            groupSend.joinSuccessSend(group.getHeadId(), group.getGroupCode(), groupBo.getNickName(), groupBo.getProductName());
        }
        if (Objects.equals(groupMember.getGroupRole(), GroupRoleEnum.INTRUDER.getCode())) {
            groupMemberDao.updateOrderStatus(GroupOrderStatusEnum.WAIT_DELIVER.getCode(), MessageEnum.invalid_flag.getCode(), Lists.newArrayList(groupBo.getUserId()), group.getId());
            groupSend.sendValidGroupMsgToMq(buildValidBo(groupMember));
        }
        //成团成功
        if (groupSuccess) {
            groupSend.groupSuccessSend(group.getGroupCode(), userIds);
            //批量刷新用户正在拼团的个数
            userIds.forEach(userId -> {
                groupMemberCache.delCurrentGroupList(userId);
            });
        }
    }

    /**
     * 构建成团和插入的消息体
     *
     * @param groupMember
     * @return
     */
    private ValidBo buildValidBo(GroupMember groupMember) {
        ValidBo validBo = new ValidBo();
        validBo.setType(MessageEnum.delivery.getCode());
        validBo.setOrderNo(groupMember.getOrderNo());
        return validBo;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResultVO<ShareGroupTo> createShareGroup(ResultVO<ShareGroupTo> result, Group group, GroupMember groupMember, GroupBo groupBo, ShareGroupTo shareGroupTo, String groupCode) {
        //插入Group
        groupDao.insertGroupSelectKey(group);
        groupMember.setGroupId(group.getId());
        //插入团成员
        groupMemberDao.insertSelective(groupMember);
        //刷新缓存
        groupMemberCache.delCurrentGroupList(groupBo.getUserId());
        shareGroupTo.setType(0);
        shareGroupTo.setGroupCode(groupCode);
        return result.format(Boolean.TRUE, "参团成功", shareGroupTo);
    }
}
