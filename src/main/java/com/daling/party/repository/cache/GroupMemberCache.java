package com.daling.party.repository.cache;

import com.daling.party.domain.entity.GroupMember;
import com.daling.party.domain.group.enums.GroupStatusEnum;
import com.daling.party.repository.cache.key.CacheCommons;
import com.daling.party.repository.cache.key.RedisKeyTemplate;
import com.daling.party.repository.dao.GroupDao;
import com.daling.party.repository.dao.GroupMemberDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GroupMemberCache {

    @Autowired
    private GroupDao groupDao;

    @Resource
    private GroupMemberDao groupMemberDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private RedisTemplate<String, List<GroupMember>> memberListRedis;

    /**
     * 查询正在拼团的个数
     * 算法：缓存某个用户正在拼团的团结束时间，根据团结束时间动态计算团个数。
     * 目的：减少查询数据库次数，保证数据实时准确（定时器同步会有延迟、团可能随时结束）。
     * @param userId
     * @return
     */
    public int userCurrentGroupCount(long userId) {
        log.info("userCurrentGroupCount userId={}",userId);
        Object object = redisTemplate.opsForValue().get(RedisKeyTemplate.currentGroupListKey(userId));
        List<Long> endTimes;
        if (null == object) {
            endTimes = groupDao.selectCurrentUserGroupEndTimeList(userId, GroupStatusEnum.PROCESSING.getCode());
            redisTemplate.opsForValue().set(RedisKeyTemplate.currentGroupListKey(userId), endTimes, 1, TimeUnit.DAYS);
        } else {
            endTimes = (List<Long>) object;
            endTimes.removeIf(e -> (System.currentTimeMillis() > (e * 1000)));
        }
        return endTimes.size();
    }

    public void delCurrentGroupList(long userId) {
        log.info("delCurrentGroupList userId={}",userId);
        redisTemplate.delete(RedisKeyTemplate.currentGroupListKey(userId));
    }

    /**
     * List<GroupMember> 查询缓存
     *
     * @param groupId
     * @return
     */
    public List<GroupMember> queryMemberListByCache(Long groupId, Integer isJoin) {
        String memberListKey = String.format(RedisKeyTemplate.PT_GROUP_MEMBER_LIST_KEY, groupId, isJoin);
        List<GroupMember> groupMembers = memberListRedis.opsForValue().get(memberListKey);
        if (Objects.nonNull(groupMembers)) {
            log.info("members走缓存");
            return groupMembers;
        }
        groupMembers = groupMemberDao.queryGroupMemberList(groupId, isJoin);
        if (CollectionUtils.isNotEmpty(groupMembers)) {
            memberListRedis.opsForValue().set(memberListKey, groupMembers, CacheCommons.DAY_DURATION, TimeUnit.SECONDS);
        }
        return groupMembers;
    }

    /**
     * 批量刷新成员
     * @param groupId
     */
    public void refreshMemberList(Long groupId, Integer isJoin) {
        String memberListKey = String.format(RedisKeyTemplate.PT_GROUP_MEMBER_LIST_KEY, groupId, isJoin);
        List<GroupMember> groupMembers = groupMemberDao.queryGroupMemberList(groupId, isJoin);
        if (CollectionUtils.isNotEmpty(groupMembers)) {
            memberListRedis.opsForList().leftPush(memberListKey, groupMembers);
            memberListRedis.expire(memberListKey, CacheCommons.DAY_DURATION, TimeUnit.SECONDS);
        } else {
            memberListRedis.delete(memberListKey);
        }
    }

    /**
     * 删除成员
     *
     * @param groupId
     */
    public void deleteMemberList(Long groupId, Integer isJoin) {
        String memberListKey = String.format(RedisKeyTemplate.PT_GROUP_MEMBER_LIST_KEY, groupId, isJoin);
        memberListRedis.delete(memberListKey);
    }

    public Long clearRedisCash(String key){
        return redisTemplate.delete(redisTemplate.keys(key + "*"));
    }
}
