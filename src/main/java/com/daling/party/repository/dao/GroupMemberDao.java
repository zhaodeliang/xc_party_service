package com.daling.party.repository.dao;

import com.daling.party.domain.entity.GroupMember;
import com.daling.party.domain.vo.PageVO;
import com.daling.party.service.group.bo.ExtGroupMember;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

public interface GroupMemberDao extends BaseMapper<GroupMember> {

    // 查询用户最近一次的拼团Id
    @Select("SELECT gm.group_id FROM s_group_member gm WHERE gm.user_id = #{userId} AND gm.is_join = 1 ORDER BY gm.create_time DESC LIMIT 0, 1")
    Integer selectGroupLastGroupByUserId(@Param("userId") Long userId);

    List<GroupMember> queryGroupMemberList(@Param("groupId") Long groupId, @Param("isJoin") Integer isJoin);

    List<ExtGroupMember> selectValidGroupMember(@Param("validFlag") Integer validFlag, @Param("pageVO") PageVO pageVO);

    int updateGroupMemberValidFlag(@Param("validFlag") Integer validFlag, @Param("id") long id);

    /**
     * 通过团Id和用户Id查询
     * @param groupId 团Id
     * @param userId 用户Id
     * @return 团员信息
     */
    @Select("SELECT * FROM s_group_member gm WHERE gm.group_id = #{groupId} AND gm.user_id = #{userId} AND gm.is_join = 1")
    @ResultMap("BaseResultMap")
    GroupMember selectByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);

    int updateOrderStatus(@Param("orderStatus") Integer orderStatus, @Param("validFlag") Integer validFlag, @Param("userIds") List<Long> userIds, @Param("groupId") Long groupId);

    GroupMember queryMember(@Param("groupId") Long groupId, @Param("orderNo") String orderNo);

}
