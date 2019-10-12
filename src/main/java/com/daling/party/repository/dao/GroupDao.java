package com.daling.party.repository.dao;

import com.daling.party.domain.entity.Group;
import com.daling.party.domain.vo.PageVO;
import com.daling.party.infrastructure.base.BaseDao;
import com.daling.party.service.group.bo.GroupMsgBo;
import com.daling.party.service.group.bo.UserGroupBo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface GroupDao extends BaseDao<Group> {

    /**
     * 通过活动编码获取拼团列表
     * @param activityCode
     * @param pageVO
     * @return
     */
    List<GroupMsgBo> groupListByActivityCode(@Param("activityCode") String activityCode, @Param("spu") String spu, @Param("shopId") Long shopId, @Param("pageVO") PageVO pageVO);

    /**
     * 获取用户拼团列表
     * @param userId
     * @param type
     * @param pageVO
     * @return
     */
    List<UserGroupBo> userGroupList(@Param("userId") Long userId, @Param("type") int type, @Param("pageVO") PageVO pageVO, @Param("now") Date now);
    /**
     * 获取用户正在拼团的结束时间
     * @param userId
     * @return
     */
    List<Long> selectCurrentUserGroupEndTimeList(@Param("userId") Long userId,@Param("status") int status);

    /**
     * 通过拼团活动code获取拼团Id(null表示没有拼团信息)
     * @param userId
     * @param activityCode
     * @return
     */
    @Select("SELECT g.id AS id FROM s_group g, s_group_member gm WHERE g.id = gm.group_id AND gm.user_id = #{userId} AND g.activity_code = #{activityCode} AND g.status != 0 LIMIT 0, 1")
    Long selectGroupIdByActivityCode(@Param("userId") Long userId, @Param("activityCode") String activityCode);

    /**
     * 通过拼团code获取拼团Id(null表示没有拼团信息)
     * @param userId
     * @param groupCode
     * @return
     */
    @Select("SELECT g.id AS id FROM s_group g, s_group_member gm WHERE g.id = gm.group_id AND gm.user_id = #{userId} AND g.group_code = #{groupCode} LIMIT 0, 1")
    Long selectGroupIdByGroupCode(@Param("userId") Long userId, @Param("groupCode") String groupCode);

    /**
     * 获取团的基本信息
     * @param groupCode
     * @return
     */
    GroupMsgBo loadGroupMessage(@Param("groupCode") String groupCode);

    /**
     * 查询团信息
     * @param activityCode
     * @param userId
     * @param spu
     * @param groupType
     * @param groupStatus
     * @return
     */
    Group queryUniqShareGroup(@Param("activityCode") String activityCode,
                              @Param("userId") Long userId,
                              @Param("spu") String spu,
                              @Param("groupType") Integer groupType,
                              @Param("groupStatus") Integer groupStatus);

    /**
     * 通过groupCode查询Group
     * @param groupCode
     * @return
     */
    Group queryGroupByCode(@Param("groupCode") String groupCode);

    /**
     * 批量查询 GROUP
     *
     * @param groupCodes
     * @return
     */
    List<Group> batchQueryGroup(@Param("groupCodes") List<String> groupCodes);

    /**
     * 修改过期的拼团状态
     * @return
     */
    int batchUpdateGroupEndTimeStatus(@Param("ids") List<Long> ids,@Param("status") int status);

    /**
     * 查询过期的拼团
     * @return
     */
    List<Group> selectGroupEndTimeStatus();

    /**
     * 插入团信息 返回主键id
     *
     * @param group
     * @return
     */
    Long insertGroupSelectKey(Group group);

    /**
     * 查询即将结束,团列表
     * @return
     */
    @Select("SELECT * FROM s_group g WHERE g.end_time <= #{endGroupEndDate} AND g.remaining_msg_push_status in (0,6) AND g.end_time > now() LIMIT 0, 1000")
    @ResultMap("BaseResultMap")
    List<Group> selectRemainingGroupList(@Param("endGroupEndDate") Date endGroupEndDate);

    /**
     * 团开始6小时，团列表
     * @return
     */
    @Select("SELECT * FROM s_group g WHERE now() >= date_add(g.start_time, interval 6 hour) AND g.remaining_msg_push_status = 0 AND surplus_num != 0 AND g.end_time > now() LIMIT 0, 1000")
    @ResultMap("BaseResultMap")
    List<Group> selectStart6HourGroupList();

    /**
     * 修改推送状态
     * @param groupId
     * @param remainingMsgPushStatus
     */
    @Update("UPDATE s_group SET remaining_msg_push_status = #{remainingMsgPushStatus}, update_time = now() WHERE id = #{groupId}")
    void updateRemainingMsgPushStatus(@Param("groupId") Long groupId, @Param("remainingMsgPushStatus") Integer remainingMsgPushStatus);

    /**
     * 通过groupCode查询Group
     * @param groupCode
     * @return
     */
    @Select("SELECT * FROM s_group g WHERE g.group_code = #{groupCode}")
    @ResultMap("BaseResultMap")
    Group selectGroupByCode(@Param("groupCode") String groupCode);
}
