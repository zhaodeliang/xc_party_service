package com.daling.party.controller.group;

import com.daling.common.dmonitor.DMonitor;
import com.daling.party.controller.dto.UserDto;
import com.daling.party.controller.group.converter.GroupConverter;
import com.daling.party.controller.group.converter.GroupMsgConverter;
import com.daling.party.controller.group.converter.UserGroupConverter;
import com.daling.party.controller.group.resp.GroupDetailResp;
import com.daling.party.controller.group.resp.UserGroupListResp;
import com.daling.party.domain.vo.PageVO;
import com.daling.party.infrastructure.base.BaseController;
import com.daling.party.infrastructure.enums.BooleanEnum;
import com.daling.party.infrastructure.utils.AssertUtil;
import com.daling.party.infrastructure.utils.XcHeadWrapper;
import com.daling.party.service.group.GroupService;
import com.daling.party.service.group.bo.GroupBo;
import com.daling.party.service.group.bo.GroupMsgBo;
import com.daling.party.service.group.bo.UserGroupBo;
import com.daling.ucclient.clients.UserShopClient;
import com.daling.ucclient.enums.YesNoEnum;
import com.daling.ucclient.pojo.User;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("group/")
@Slf4j
public class GroupController extends BaseController{

    @Autowired
    private GroupService groupService;

    // 查询拼团列表（商品详情页面）
    @GetMapping(value = "groupListGoodsDetail.do")
    public XcHeadWrapper<PageVO> groupListByActivityCode(HttpServletRequest request, @RequestParam String activityCode, @RequestParam String sku, PageVO pageVO){
        Stopwatch stopwatch = Stopwatch.createStarted();
        pageVO.checkPageVO();
        List<GroupMsgBo> listBo = groupService.groupListByActivityCode(request,activityCode, sku, pageVO);
        // 列表第一个是拼团中，去除列表中的拼团成功
        if (CollectionUtils.isNotEmpty(listBo) && listBo.get(0).getSurplusNum() > 0) {
            listBo.removeIf(groupMsgBo -> groupMsgBo.getSurplusNum() == 0);
        }
        pageVO.buildPageData(GroupMsgConverter.bo2GoodsDetailResp(listBo));
        pageVO.setHasNext(false);
        DMonitor.recordOne("api_groupListGoodsDetail", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return buildResponse(pageVO);
    }

    // 我的拼团列表 type : 0、全部1、成功 2、失败 3、进行中
    @GetMapping(value = "myGroupList.do")
    public XcHeadWrapper<PageVO> myGroupList(UserDto userDto, @RequestParam(defaultValue = "0") int type, PageVO pageVO){
        Stopwatch stopwatch = Stopwatch.createStarted();
        pageVO.checkPageVO();

        List<UserGroupBo> listBo = groupService.userGroupList(userDto.getId(), type, pageVO);
        pageVO.buildPageData(UserGroupConverter.bo2UserGroupListResp(listBo));
        DMonitor.recordOne("api_myGroupList", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return buildResponse(pageVO);
    }

    // 检查用户是否开团---暂不用
    // @GetMapping(value = "checkUserGroupByActivityCode.do")
    public XcHeadWrapper<Long> checkUserGroupByActivityCode(UserDto userDto, @RequestParam String activityCode){
        Stopwatch stopwatch = Stopwatch.createStarted();

        Long groupId = groupService.loadGroupByActivityCode(userDto.getId(), activityCode);
        DMonitor.recordOne("api_checkUserGroupByActivityCode", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return buildResponse(groupId);
    }

    // 当前用户在微信内有过参团或开团拼团列表
    @GetMapping(value = "checkUserWxIsHasGroup.do")
    public XcHeadWrapper<Integer> checkUserWxIsHasGroup(UserDto userDto){
        Stopwatch stopwatch = Stopwatch.createStarted();
        Integer isHasGroup = BooleanEnum.False.getCode();
        if (StringUtils.isNotEmpty(userDto.getUnionid())) {
            Long wxUserId = getTouchHistoryUserId(userDto);
            if (wxUserId != null) {
                isHasGroup = groupService.loadUserIsHasGroup(wxUserId);
            }
        }
        DMonitor.recordOne("api_checkUserWxIsHasGroup", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return buildResponse(isHasGroup);
    }

    // 获取当前用户微信拼团订单列表
    @GetMapping(value = "myWxGroupList.do")
    public XcHeadWrapper<PageVO> myWxGroupList(UserDto userDto, @RequestParam(defaultValue = "0") int type, PageVO pageVO){
        Stopwatch stopwatch = Stopwatch.createStarted();
        pageVO.checkPageVO();

        Long wxUserId = null;
        if (StringUtils.isNotEmpty(userDto.getUnionid())) {
            wxUserId = getTouchHistoryUserId(userDto);
        }
        List<UserGroupListResp> respList = Lists.newArrayList();
        if (wxUserId != null) {
            List<UserGroupBo> listBo = groupService.userGroupList(wxUserId, type, pageVO);
            respList = UserGroupConverter.bo2UserGroupListResp(listBo);
        }
        pageVO.buildPageData(respList);
        DMonitor.recordOne("api_myWxGroupList", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return buildResponse(pageVO);
    }

    // 通过unionid获取是否有其他用户
    private Long getTouchHistoryUserId(UserDto userDto) {
        List<User> users = UserShopClient.getUsersByUnionid(userDto.getUnionid());
        if (CollectionUtils.isNotEmpty(users)) {
            List<Long> userIdList = Lists.newArrayList();
            users.stream().filter(item -> !Objects.equals(userDto.getId(), item.getId()) && item.getStatus() == 1)
                    .forEach(item -> userIdList.add(item.getId()));
            return CollectionUtils.isNotEmpty(userIdList) ? userIdList.get(0): null;
        }
        return null;
    }

    /**
     * 查询拼团详情
     *
     * @param userDto
     * @param groupCode
     * @return
     */
    @RequestMapping(value = "queryGroupDetail.do", method = RequestMethod.GET)
    public XcHeadWrapper<GroupDetailResp> queryGroupDetail(UserDto userDto, String groupCode, String orderNo) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        AssertUtil.notNull(userDto.getId(), "userId is null");
        AssertUtil.notEmpty(groupCode, "groupCode is null");
        AssertUtil.notNull(userDto.getUserType().getCode(), "userType is null");
        GroupBo groupBo = groupService.queryGroupDetail(userDto, groupCode, orderNo);
        DMonitor.recordOne("api_query_group_detail", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        if (Objects.isNull(groupBo)) {
            return buildAsyResponse();
        } else {
            Integer isJoin = groupBo.getIsJoin();
            if (Objects.equals(isJoin, YesNoEnum.NO.getCode())) {
                return buildFailJoinResponse();
            }
        }
        return buildResponse(GroupConverter.bo2Resp(groupBo));
    }
}
