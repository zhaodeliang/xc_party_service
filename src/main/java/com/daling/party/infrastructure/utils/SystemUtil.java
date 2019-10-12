package com.daling.party.infrastructure.utils;

import com.daling.party.domain.entity.Group;
import com.daling.party.domain.entity.GroupMember;
import com.daling.party.domain.group.enums.GroupJoinStatusEnum;
import com.daling.party.domain.group.enums.GroupStatusEnum;
import com.daling.party.infrastructure.enums.BooleanEnum;

import java.util.List;
import java.util.Objects;

/**
 * Created by lilindong on 2019/4/11.
 */
public class SystemUtil {

    private SystemUtil() {}

    /**
     * 判断团的状态
     * @param currentMills 当前时间戳
     * @param endMills 团结束时间出
     * @param surplusNum 剩余成团数量
     * @return 状态值
     */
    public static Integer judgeGroupTrueStatus(long currentMills, long endMills, int surplusNum) {
        if(surplusNum == 0){
            return GroupStatusEnum.SUCCESS.getCode();
        }

        if(endMills >= currentMills){
            return GroupStatusEnum.PROCESSING.getCode();
        }else {
            return GroupStatusEnum.FAIL.getCode();
        }
    }

    /**
     * 根据团信息获取团的加入状态
     * @param group 团实体
     * @return 团加入状态
     */
    public static Integer judgeGroupJoinStatus(Group group) {
        if (group == null || Objects.equals(group.getStatus(), GroupStatusEnum.DRAFT.getCode())) { // 团不存在
            return GroupJoinStatusEnum.NO_EXITS.getCode();
        }
        long currentMills = System.currentTimeMillis();
        if (currentMills < group.getStartTime().getTime()) { // 团未开始
            return GroupJoinStatusEnum.NO_START.getCode();
        }
        if (currentMills >= group.getEndTime().getTime() && group.getSurplusNum() != 0) { // 拼团失败
            return GroupJoinStatusEnum.GROUP_FAIL.getCode();
        }
        if (group.getSurplusNum() == 0) { // 已成团
            if (!Objects.equals(group.getJoinNum(), group.getUpperLimit())) { // 已成团，但未达到人数上限
                if (currentMills < group.getEndTime().getTime()) { // 已成团，但未达到人数上限，且未达到拼团结束时间
                    return GroupJoinStatusEnum.GROUP_SUCCESS.getCode();
                } else { // 已成团，但未达到人数上限，而且拼团已经结束
                    return GroupJoinStatusEnum.GROUP_SUCCESS_END.getCode();
                }
            } else { // 已成团，且已到达人数上限
                if (currentMills < group.getEndTime().getTime()) { // 已成团，且已到达人数上限，但未达到拼团结束时间
                    return GroupJoinStatusEnum.GROUP_SUCCESS_UPPER.getCode();
                } else {
                    return GroupJoinStatusEnum.GROUP_SUCCESS_END_UPPER.getCode(); // 已成团，且已到达人数上限，而且拼团已经结束
                }
            }
        }
        return GroupJoinStatusEnum.PROCESSING.getCode();
    }


    /**
     * 判断用户是否加入团
     * @param userId 用户Id
     * @param groupMemberList 团成员列表
     * @return
     */
    public static Integer judgeUserIsJoinGroup(Long userId, List<GroupMember> groupMemberList) {
        long size = groupMemberList.stream().filter(item -> Objects.equals(item.getUserId(), userId)).count();
        return size == 0 ? BooleanEnum.False.getCode() : BooleanEnum.True.getCode();
    }
}
