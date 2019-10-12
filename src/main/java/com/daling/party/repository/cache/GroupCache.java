package com.daling.party.repository.cache;

import com.daling.common.dmonitor.DMonitor;
import com.daling.party.domain.entity.Group;
import com.daling.party.domain.vo.PageVO;
import com.daling.party.repository.cache.key.CacheCommons;
import com.daling.party.repository.cache.key.RedisKeyTemplate;
import com.daling.party.repository.dao.GroupDao;
import com.daling.party.service.group.bo.GroupMsgBo;
import com.daling.ucclient.clients.UserShopClient;
import com.daling.ucclient.enums.UserTypeEnum;
import com.daling.ucclient.pojo.AuthInfo;
import com.daling.ucclient.pojo.Shop;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.testng.util.Strings;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GroupCache {

    @Autowired
    private GroupDao groupDao;
    @Resource
    private RedisTemplate<String, List<GroupMsgBo>> redisTemplate;

    @Resource
    private RedisTemplate<String, Group> groupRedis;

    public List<GroupMsgBo> groupListByActivityCode(AuthInfo auth, String activityCode, String spu, PageVO pageVO) {
        Long shopId = getShopId(auth);
        String key = RedisKeyTemplate.GROUP_LIST_ACTIVITY_CODE_KEY;
        key = String.format(key, activityCode, spu, pageVO.getPageIndex(), pageVO.getPageSize(),shopId);
        BoundValueOperations<String, List<GroupMsgBo>> ops  = redisTemplate.boundValueOps(key);
        List<GroupMsgBo> result = ops.get();
        if (CollectionUtils.isEmpty(result)) {
            log.info(key + "缓存失效，查询mysql");
            result = groupListByCondition(shopId,activityCode, spu, pageVO);
            if(Objects.nonNull(result)){
                ops.set(result, 10, TimeUnit.SECONDS);
            }
        }
        return result;
    }

    List<GroupMsgBo> groupListByCondition(Long shopId,String activityCode, String spu, PageVO pageVO){
        log.info("groupListByCondition,activityCode={},spu={},pageVO={}",activityCode,spu,pageVO);

        List<GroupMsgBo> groupMsgBos;

        if(null != shopId){
            groupMsgBos = groupDao.groupListByActivityCode(activityCode, spu, shopId, pageVO);
            if(CollectionUtils.isNotEmpty(groupMsgBos)){
                log.info("groupListByCondition shopId={},groupMsgBos.size={}",shopId,groupMsgBos.size());
                return groupMsgBos;
            }
        }

        groupMsgBos = groupDao.groupListByActivityCode(activityCode, spu, null, pageVO);
        if(CollectionUtils.isNotEmpty(groupMsgBos)){
            log.info("groupListByCondition shopId is null,groupMsgBos.size={}",groupMsgBos.size());
            return groupMsgBos;
        }

        return groupMsgBos;
    }

    private Long getShopId(AuthInfo auth) {
        Long shopId = null;
        if(null != auth && null != auth.getId()){
            Long uId = auth.getId();
            try {
                Stopwatch stopwatch = Stopwatch.createStarted();
                if(Objects.equals(UserTypeEnum.SHOPKEEPER.getCode(),auth.getUserType().getCode())){
                    Shop shop = UserShopClient.getShopByOwnerId(uId);
                    shopId = Objects.nonNull(shop)?shop.getId():null;
                    log.info("groupListByCondition userType is shopkeeper,userId={}, shopId={}",uId,shopId);
                }else{
                    Shop shop = UserShopClient.getShopByInviteCode(auth.getFollowerInviteCode());
                    shopId = Objects.nonNull(shop)?shop.getId():null;
                    log.info("groupListByCondition userType is fans,userId={}, shopId={}",uId,shopId);
                }
                DMonitor.recordOne("api_groupListByCondition_uc_shop", stopwatch.elapsed(TimeUnit.MILLISECONDS));
            } catch (Exception e) {
                log.error("groupListByCondition error ,message="+e.getMessage(),e);
            }
        }
        return shopId;
    }

    /**
     * Group 查询缓存
     *
     * @param groupCode 团code
     * @return
     */
    public Group queryGroupByCache(String groupCode) {
        String groupKey = String.format(RedisKeyTemplate.PT_GROUP_INFO_KEY, groupCode);
        Group group = groupRedis.opsForValue().get(groupKey);
        if (Objects.nonNull(group) && Objects.nonNull(group.getEndTime())) {
            return group;
        }
        group = groupDao.queryGroupByCode(groupCode);
        if (Objects.nonNull(group)) {
            groupRedis.opsForValue().set(groupKey, group, CacheCommons.DAY_DURATION, TimeUnit.SECONDS);
        }
        return group;
    }


    /**
     * Group 刷新缓存
     *
     * @param groupCode
     */
    public void refreshGroup(String groupCode) {
        String groupKey = String.format(RedisKeyTemplate.PT_GROUP_INFO_KEY, groupCode);
        Group group = groupDao.queryGroupByCode(groupCode);
        if (Objects.nonNull(group)) {
            groupRedis.opsForValue().set(groupKey, group, CacheCommons.DAY_DURATION, TimeUnit.SECONDS);
        } else {
            groupRedis.delete(groupKey);
        }
    }

    /**
     * 批量刷新group
     *
     * @param groups
     */
    public void batchRefreshGroup(List<Group> groups) {
        if (CollectionUtils.isNotEmpty(groups)) {
            groups.forEach(group -> {
                String groupKey = String.format(RedisKeyTemplate.PT_GROUP_INFO_KEY, group.getGroupCode());
                groupRedis.opsForValue().set(groupKey, group, CacheCommons.DAY_DURATION, TimeUnit.SECONDS);
            });
        }
    }

    /**
     * 批量删除group
     *
     * @param groups
     */
    public void batchDeleteGroup(List<Group> groups) {
        if (CollectionUtils.isNotEmpty(groups)) {
            groups.forEach(group -> deleteGroup(group.getGroupCode()));
        }
    }

    /**
     * 删除group
     *
     * @param groupCode
     */
    public void deleteGroup(String groupCode) {
        if (!Strings.isNullOrEmpty(groupCode)) {
            String groupKey = String.format(RedisKeyTemplate.PT_GROUP_INFO_KEY, groupCode);
            groupRedis.delete(groupKey);
        }
    }

    /**
     * 存储团开始时间：团开始后6个小时，发送团
     */


}
