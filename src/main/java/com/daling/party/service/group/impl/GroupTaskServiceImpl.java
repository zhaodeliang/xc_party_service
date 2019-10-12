package com.daling.party.service.group.impl;

import com.daling.common.lock.LockHelper;
import com.daling.common.lock.RLock;
import com.daling.party.domain.entity.Group;
import com.daling.party.domain.entity.GroupMember;
import com.daling.party.domain.group.enums.GroupMessageEnum;
import com.daling.party.domain.group.enums.GroupRoleEnum;
import com.daling.party.domain.group.enums.GroupStatusEnum;
import com.daling.party.domain.group.enums.GroupTypeEnum;
import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.domain.vo.PageVO;
import com.daling.party.domain.vo.UserVO;
import com.daling.party.feign.UCenterFeignClient;
import com.daling.party.infrastructure.enums.BooleanEnum;
import com.daling.party.infrastructure.enums.MessageEnum;
import com.daling.party.infrastructure.exception.GenericBusinessException;
import com.daling.party.infrastructure.model.ResultVO;
import com.daling.party.infrastructure.utils.AssertUtil;
import com.daling.party.infrastructure.utils.CommonUtils;
import com.daling.party.infrastructure.utils.SystemUtil;
import com.daling.party.repository.cache.GroupCache;
import com.daling.party.repository.cache.GroupMemberCache;
import com.daling.party.repository.cache.key.RedisKeyTemplate;
import com.daling.party.repository.dao.GroupDao;
import com.daling.party.repository.dao.GroupMemberDao;
import com.daling.party.service.group.GroupTaskService;
import com.daling.party.service.group.bo.ExtGroupMember;
import com.daling.party.service.group.bo.GroupRemainingMsg;
import com.daling.party.service.group.bo.ValidBo;
import com.daling.party.service.group.send.GroupSend;
import com.daling.ucclient.enums.UserTypeEnum;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;


@Service
@Slf4j
public class GroupTaskServiceImpl implements GroupTaskService {

    @Resource
    private GroupDao groupDao;

    @Resource
    private GroupCache groupCache;

    @Resource
    private GroupSend groupSend;

    @Resource
    private GroupMemberDao groupMemberDao;

    @Resource
    private GroupMemberCache groupMemberCache;

    @Resource
    private UCenterFeignClient uCenterFeignClient;

    private static final long LOCK_WAIT_TIME = 10;

    @Override
    public ResultVO<String> sendRemainingMessage() {
        log.info("sendRemainingMessage start");
        ResultVO<String> result = new ResultVO<>();

        try (RLock lock = LockHelper.getRLock(RedisKeyTemplate.PT_LOCK_NAME)) {
            if (lock.tryLock(LOCK_WAIT_TIME, TimeUnit.SECONDS)) {
                //开始时间为6小时
                handleStartTime6HourMessage();

                // 结束时间为2小时
                handleEndTime2HourMessage(System.currentTimeMillis() + 2 * 60 * 60 * 1000);
            } else {
                log.info("sendRemainingMessage lock failed : {}", RedisKeyTemplate.PT_LOCK_NAME);
                return result.format(false, "获取即将结束的拼团推送消息定时任务锁失败");
            }
        } catch (Exception e) {
            log.error("sendRemainingMessage error" + e.getMessage(), e);
            return result.format(false, e.getMessage());
        }

        log.info("sendRemainingMessage end");
        return result.format(true, "success");
    }

    /**
     * 处理距离开始时间为6小时消息
     */
    private void handleStartTime6HourMessage() {
        log.info("handleStartTime6HourMessage start");

        List<Group> groups = groupDao.selectStart6HourGroupList();

        if (CollectionUtils.isNotEmpty(groups)) {
            log.info("handleStartTime6HourMessage size={}", groups.size());
            Iterator<Group> iterator = groups.iterator();
            while (iterator.hasNext()) {
                Group group = iterator.next();
                GroupRemainingMsg groupRemainingMsg = GroupRemainingMsg.builder()
                        .group_code(group.getGroupCode()).groupMessageEnum(GroupMessageEnum.GROUP_SHOPPING_UNFINISHED_6HOURS).head_id(group.getHeadId()).build();
                groupDao.updateRemainingMsgPushStatus(group.getId(), MessageEnum.message_flag.getCode());
                groupSend.sendGroupEndingMsgToMq(groupRemainingMsg);
            }
        }
    }

    /**
     * 处理结束时间为2小时消息
     *
     * @param time
     */
    private void handleEndTime2HourMessage(long time) {
        log.info("handleEndTime2HourMessage start");

        List<Group> groups = groupDao.selectRemainingGroupList(new Date(time));

        if (CollectionUtils.isNotEmpty(groups)) {
            log.info("handleEndTime2HourMessage size={}", groups.size());
            Iterator<Group> iterator = groups.iterator();
            while (iterator.hasNext()) {
                Group group = iterator.next();
                Long groupId = group.getId();
                groupDao.updateRemainingMsgPushStatus(groupId, BooleanEnum.True.getCode());

                GroupRemainingMsg groupRemainingMsg = GroupRemainingMsg.builder().group_code(group.getGroupCode()).head_id(group.getHeadId()).build();
                //已达到成团人数
                if (group.getSurplusNum() == 0 && group.getUpperLimit() > group.getJoinNum()) {
                    log.info("handleEndTime2HourMessage group success,groupId={}", groupId);
                    //获取用户信息
                    FeignResponse<UserVO> feignResponse = uCenterFeignClient.getUserById(group.getHeadId());
                    if (feignResponse.isFailure()) {
                        log.error("handleEndTime2HourMessage getUserById error,message={},groupId={}", feignResponse.getErrorMsg(), groupId);
                        throw new GenericBusinessException(feignResponse.getErrorMsg());
                    }
                    Integer userType = feignResponse.getData().getUserType();
                    if (UserTypeEnum.SHOPKEEPER.getCode() == userType) {
                        log.info("handleEndTime2HourMessage group success and userType is Shopkeeper,groupId={}", groupId);
                        groupRemainingMsg.setGroupMessageEnum(GroupMessageEnum.GROUP_SHOPPING_FINISHED_REMAINING_2HOURS);
                        groupSend.sendGroupEndingMsgToMq(groupRemainingMsg);
                    }
                }
                //未达到成团人数，并且为分享团
                else if (Objects.equals(group.getGroupType(), GroupTypeEnum.SHARE_GROUP.getCode())) {
                    log.info("handleEndTime2HourMessage group not success,groupType is share group,groupId={}", groupId);
                    groupRemainingMsg.setGroupMessageEnum(GroupMessageEnum.GROUP_SHOPPING_SHARE_UNFINISHED_REMAINING_2HOURS);
                    groupSend.sendGroupEndingMsgToMq(groupRemainingMsg);
                }
                //未达到成团人数，并且为自购团
                else if (Objects.equals(group.getGroupType(), GroupTypeEnum.BUY_GROUP.getCode())) {
                    log.info("handleEndTime2HourMessage group not success,groupType is self-buying group,groupId={}", groupId);
                    groupRemainingMsg.setGroupMessageEnum(GroupMessageEnum.GROUP_SHOPPING_SELF_UNFINISHED_REMAINING_2HOURS);
                    groupSend.sendGroupEndingMsgToMq(groupRemainingMsg);
                }
            }
        }
    }

    @Override
    public ResultVO handlerValidGroupMember() {
        ResultVO resultVO = new ResultVO();
        try (RLock zlock = LockHelper.getRLock(RedisKeyTemplate.PT_HANDLER_VALID_GROUP_MEMBER)) {
            if (zlock.tryLock(LOCK_WAIT_TIME, TimeUnit.SECONDS)) {
                List<ExtGroupMember> groupMembers = groupMemberDao.selectValidGroupMember(MessageEnum.valid_flag.getCode(), new PageVO(1, 200));
                if (CommonUtils.objectIsNotEmpty(groupMembers)) {
                    log.info("selectGroupMemberValid size = {}", groupMembers.size());
                    //推送发货、退款消息
                    Set<ExtGroupMember> members = pushGroupMemberFinallyMessage(groupMembers);
                    //推送拼团失败消息
                    pushFailedGroupMessage(members);
                }

                //修改过期的拼团状态
                updateGroupEndTimeStatus();
            } else {
                log.error("获取锁失败");
                return resultVO.format(false, "获取锁失败");
            }
        } catch (Exception e) {
            log.error("handlerValidGroupMember 执行异常，异常信息=" + e.getMessage(), e);
            return resultVO.format(false, e.getMessage());
        }
        return resultVO.format(true, "success");
    }

    /**
     * 团过期置为失效
     */
    private void updateGroupEndTimeStatus() {
        List<Group> groups = groupDao.selectGroupEndTimeStatus();
        List<Long> ids = groups.stream().map(Group::getId).collect(Collectors.toList());
        if (CommonUtils.objectIsNotEmpty(ids)) {
            int updateNum = groupDao.batchUpdateGroupEndTimeStatus(ids, GroupStatusEnum.FAIL.getCode());
            log.info("batchUpdateGroupEndTimeStatus num={}", updateNum);
            //刷新缓存
            groups.forEach(e -> e.setStatus(GroupStatusEnum.FAIL.getCode()));
            groupCache.batchDeleteGroup(groups);
        }
    }

    /**
     * 推送拼团失败消息
     *
     * @param groupMembers
     */
    private void pushFailedGroupMessage(Set<ExtGroupMember> groupMembers) {
        //不可为空
        boolean allFailure = CommonUtils.objectIsEmpty(groupMembers) || (groupMembers.size() == 1 && groupMembers.contains(null));
        AssertUtil.isTrue(allFailure, "推送消息异常:" + groupMembers.toString());

        Map<String, List<ExtGroupMember>> groupMap = groupMembers.stream()
                .filter(e -> (
                        (null != e) && (GroupStatusEnum.FAIL.getCode() == SystemUtil.judgeGroupTrueStatus(System.currentTimeMillis(), e.getEndTime().getTime(), e.getSurplusNum())))
                )
                .collect(Collectors.groupingBy(ExtGroupMember::getGroupCode));

        groupMap.forEach((k, v) -> {
            List members = v.stream().map(GroupMember::getUserId).collect(Collectors.toList());
            groupSend.sendGroupShoppingFailMessage(k, members);
        });
    }

    /**
     * 多线程推送发货、退款消息
     *
     * @param groupMembers
     * @return
     * @throws Exception
     */
    private Set<ExtGroupMember> pushGroupMemberFinallyMessage(List<ExtGroupMember> groupMembers) throws Exception {

        ExecutorService pool = new ThreadPoolExecutor(2, 200, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200));
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(pool);

        List<ListenableFuture<ExtGroupMember>> futures = Lists.newArrayList();
        groupMembers.forEach(e -> futures.add(executorService.submit(new HandlerGroupMemberRunnable(e))));

        groupMembers = Futures.successfulAsList(futures).get();
        log.info("pushGroupMemberFinallyMessage success size={}", groupMembers.size());

        pool.shutdown();

        return groupMembers.stream().collect(Collectors.toSet());

    }

    /**
     * function : 推送发货、退款消息
     */
    private class HandlerGroupMemberRunnable implements Callable<ExtGroupMember> {

        private ExtGroupMember groupMember;
        private Long userId;
        private String orderNo;
        private GroupRoleEnum groupRoleEnum;

        public HandlerGroupMemberRunnable(ExtGroupMember groupMember) {
            Validate.notNull(groupMember);
            this.groupMember = groupMember;
            this.userId = groupMember.getUserId();
            this.orderNo = groupMember.getOrderNo();
            this.groupRoleEnum = GroupRoleEnum.codeOf(groupMember.getGroupRole());
        }

        @Override
        public ExtGroupMember call() {

            try {
                checkParams();

                groupMemberCache.delCurrentGroupList(userId);
                int updateFlag = groupMemberDao.updateGroupMemberValidFlag(MessageEnum.invalid_flag.getCode(), groupMember.getId());
                log.info("memberRunnable updateFlag={}，orderNo={},userId={}", updateFlag, orderNo, userId);

                if (StringUtils.isBlank(groupMember.getOrderNo())) {
                    log.info("memberRunnable userId={},roleType={},orderNo is blank", userId, groupRoleEnum.getName());
                    return groupMember;
                }
                //发送mq消息
                groupSend.sendValidGroupMsgToMq(buildMsgContent());
                return groupMember;
            } catch (Exception e) {
                log.error("memberRunnable error message={}" + e.getMessage(), e);
                return null;
            }
        }

        private ValidBo buildMsgContent() {
            ValidBo validBo = new ValidBo();
            validBo.setOrderNo(groupMember.getOrderNo());

            //拼团状态
            long currentMills = System.currentTimeMillis();
            long groupEndTime = groupMember.getEndTime().getTime();
            Integer status = SystemUtil.judgeGroupTrueStatus(currentMills, groupEndTime, groupMember.getSurplusNum());
            log.info("memberRunnable userId={},orderNo={},currentMills={},groupEndTime={},surplusNum={},status={}",
                    userId, orderNo, currentMills, groupEndTime, groupMember.getSurplusNum(), groupRoleEnum.getName(), GroupStatusEnum.codeOf(status).getName());

            //拼团成功
            if (GroupStatusEnum.SUCCESS.getCode() == status) {
                log.info("memberRunnable userId={},orderNo={}, group success ,should delivery", userId, orderNo);
                validBo.setType(MessageEnum.delivery.getCode());
            }
            //失败
            else {
                log.info("memberRunnable userId={},orderNo={}, group failed ,should refund", userId, orderNo);
                validBo.setType(MessageEnum.refund.getCode());
            }
            return validBo;
        }

        private void checkParams() {
            //团长创建草稿团 orderNo 可为空，否则不可为空
            boolean orderNoNotBlank = groupMember.getGroupRole() != GroupRoleEnum.HEAD.getCode() ? StringUtils.isNotBlank(groupMember.getOrderNo()) : true;
            boolean groupStatusNotNull = null != groupMember.getGroupStatus();
            boolean groupIdNotNull = null != groupMember.getGroupId();
            boolean groupCodeNotNull = StringUtils.isNotBlank(groupMember.getGroupCode());
            checkArgument(orderNoNotBlank && groupStatusNotNull && groupIdNotNull && groupCodeNotNull, "参数错误" + groupMember.toString());
        }
    }
}
