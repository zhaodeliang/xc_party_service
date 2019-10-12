package com.daling.party.service.group.converter;

import com.daling.party.domain.entity.Group;
import com.daling.party.domain.group.enums.GroupStatusEnum;
import com.daling.party.infrastructure.utils.SystemUtil;
import com.daling.party.service.group.bo.GroupBo;
import com.daling.party.service.group.bo.GroupMsgBo;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author jiwei.xue
 * @date 2019/4/9 15:06
 */
public class GroupMsgConverter {

    public static GroupMsgBo entity2Bo(Group group) {
        if (group == null || Objects.equals(group.getStatus(), GroupStatusEnum.DRAFT.getCode())) {
            return null;
        }
        Integer status = SystemUtil.judgeGroupTrueStatus(System.currentTimeMillis(), group.getEndTime().getTime(), group.getLowerLimit());
        return GroupMsgBo.builder()
                .groupCode(group.getGroupCode()).status(status)
                .groupType(group.getGroupType()).groupRule(group.getGroupRule())
                .upperLimit(group.getUpperLimit()).lowerLimit(group.getLowerLimit())
                .surplusNum(group.getSurplusNum()).joinNum(group.getJoinNum())
                .startTime(group.getStartTime()).endTime(group.getEndTime())
                .headUserId(group.getHeadId()).createTime(group.getCreateTime())
                .build();
    }


}
