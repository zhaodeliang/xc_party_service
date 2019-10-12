package com.daling.party.service.group;

import com.daling.party.controller.dto.UserDto;
import com.daling.party.controller.group.resp.GroupJoinStatusResp;
import com.daling.party.domain.entity.Group;
import com.daling.party.domain.vo.PageVO;
import com.daling.party.infrastructure.exception.GenericBusinessException;
import com.daling.party.infrastructure.model.ResultVO;
import com.daling.party.service.group.bo.*;
import com.daling.party.service.group.to.ShareGroupTo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface GroupService {

    /**
     * 通过活动编码获取拼团列表
     * @param activityCode
     * @param sku
     * @param pageVO
     * @return
     */
    List<GroupMsgBo> groupListByActivityCode(HttpServletRequest request, String activityCode, String sku, PageVO pageVO);

    /**
     * 获取用户拼团列表
     * @param userId
     * @param type
     * @param pageVO
     * @return
     */
    List<UserGroupBo> userGroupList(long userId, int type, PageVO pageVO);

    /**
     * 获取用户拼团个数
     * @param userId
     * @return
     */
    Integer userGroupCount(long userId);

    /**
     * 清除指定缓存
     *
     * @param redisKey
     * @return
     */
    Long clearRedisCash(String redisKey);

    /**
     * 通过拼团code获取拼团Id(null表示没有拼团信息)
     * @param userId
     * @param activityCode
     * @return
     */
    Long loadGroupByActivityCode(Long userId, String activityCode);

    /**
     * 判断当前用户是否有
     * @param userId
     * @return
     */
    Integer loadUserIsHasGroup(Long userId);

    /**
     * 根据团编码活动团信息
     * @param groupCode
     * @param orderNo
     * @return
     */
    GroupMsgBo loadGroupMessage(String groupCode, String orderNo);

    /**
     * 根据团编码获取团的加入状态
     * @param groupCode
     * @param userId
     * @return
     */
    GroupJoinStatusResp loadGroupJoinStatus(String groupCode, Long userId);

    /**
     * 创建草稿团
     * @param userId
     * @param activityCode
     */
    ResultVO<String> createDraftGroup(Long userId, String activityCode, String spu);

    /**
     * 创建分享团
     * @param groupBo
     * @return
     */
    ResultVO<ShareGroupTo> createShareGroup(GroupBo groupBo);

    /**
     * 查询拼团详情
     * @param userDto
     * @param groupCode
     * @return
     */
    GroupBo queryGroupDetail(UserDto userDto, String groupCode, String orderNo) throws GenericBusinessException;

    /**
     * 查询拼团详情
     * @param groupCode
     * @return
     */
    Group getGroupByCode(String groupCode);

    /**
     * 加入团
     * @param groupBo
     */
    void joinGroup (GroupBo groupBo) throws Exception;
}
