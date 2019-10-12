package com.daling.party.service.group.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daling.common.dmonitor.DMonitor;
import com.daling.common.idutils.IDGener;
import com.daling.common.lock.LockHelper;
import com.daling.common.lock.RLock;
import com.daling.common.servicelocate.ServerDefIdUtil;
import com.daling.party.controller.dto.UserDto;
import com.daling.party.controller.group.resp.GroupJoinStatusResp;
import com.daling.party.domain.entity.Group;
import com.daling.party.domain.entity.GroupMember;
import com.daling.party.domain.group.constant.GroupConstant;
import com.daling.party.domain.group.enums.*;
import com.daling.party.domain.po.TGoodShelf;
import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.domain.vo.FeignResult;
import com.daling.party.domain.vo.PageVO;
import com.daling.party.domain.vo.UserVO;
import com.daling.party.feign.*;
import com.daling.party.feign.ao.PriceAo;
import com.daling.party.feign.ao.ProductAo;
import com.daling.party.feign.ao.EvaluationAo;
import com.daling.party.feign.to.*;
import com.daling.party.infrastructure.constant.SystemConstant;
import com.daling.party.infrastructure.enums.BooleanEnum;
import com.daling.party.infrastructure.enums.VisibleTypeEnum;
import com.daling.party.infrastructure.exception.GenericBusinessException;
import com.daling.party.infrastructure.model.ResultVO;
import com.daling.party.infrastructure.utils.AssertUtil;
import com.daling.party.infrastructure.utils.StringXcUtils;
import com.daling.party.infrastructure.utils.SystemUtil;
import com.daling.party.repository.cache.GroupCache;
import com.daling.party.repository.cache.GroupMemberCache;
import com.daling.party.repository.cache.key.RedisKeyTemplate;
import com.daling.party.repository.dao.GroupDao;
import com.daling.party.repository.dao.GroupMemberDao;
import com.daling.party.service.group.GroupService;
import com.daling.party.service.group.bo.*;
import com.daling.party.service.group.converter.GroupMsgConverter;
import com.daling.party.service.group.send.GroupSend;
import com.daling.party.service.group.to.ShareGroupTo;
import com.daling.ucclient.clients.UserShopClient;
import com.daling.ucclient.enums.UserTypeEnum;
import com.daling.ucclient.enums.YesNoEnum;
import com.daling.ucclient.pojo.*;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.daling.party.domain.group.enums.GroupJoinStatusEnum.NO_EXITS;
import static com.daling.party.domain.group.enums.GroupJoinStatusEnum.NO_START;


@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

    @Resource
    private GroupDao groupDao;

    @Resource
    private GroupMemberCache groupMemberCache;

    @Autowired
    private GroupCache groupCache;

    @Resource
    private GroupMemberDao groupMemberDao;

    @Resource
    private GroupTranServiceImpl groupTranService;

    @Resource
    private GroupSend groupSend;

    @Resource
    private GoodDetailFeignClient goodDetailFeignClient;

    @Resource
    private EvaluationsFeignClient evaluationsFeignClient;

    @Resource
    private ActivityFeignClient activityFeignClient;

    @Resource
    private UCenterFeignClient uCenterFeignClient;

    @Resource
    private GoodInfoFeignClient goodInfoFeignClient;

    @Resource
    private CouponFeignClient couponFeignClient;

    @Resource
    private VirtualGoodFeignClient virtualGoodFeignClient;

    @Resource
    private GoodsShelfFeignClient goodsShelfFeignClient;

    @Resource
    private PriceFeignClient priceFeignClient;

    @Override
    public List<GroupMsgBo> groupListByActivityCode(HttpServletRequest request, String activityCode, String sku, PageVO pageVO) {
        //通过xc_sale获取商品详情
        long startMillis = System.currentTimeMillis();
        log.info("调用商品中心findBySku，入参：sku={}", sku);
        FeignResponse<List<TGoodShelf>> detailListResp = goodsShelfFeignClient.findBySku(sku);
        log.info("调用商品中心findBySku成功，入参：sku={}，回文：{}，响应时间：{}", sku, JSON.toJSONString(detailListResp), System.currentTimeMillis() - startMillis);

        AssertUtil.isTrue(!detailListResp.isSuccess() || CollectionUtils.isEmpty(detailListResp.getData()), detailListResp.getErrorMsg());
        String spu = detailListResp.getData().get(0).getSpu();
        AssertUtil.notEmpty(spu, "未查询到对应的spu");

        Stopwatch stopwatch = Stopwatch.createStarted();
        AuthInfo auth = UserShopClient.auth(request);
        DMonitor.recordOne("api_groupListByActivityCode_uc_auth", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return groupCache.groupListByActivityCode(auth,activityCode, spu, pageVO);
    }


    @Override
    public List<UserGroupBo> userGroupList(long userId, int type, PageVO pageVO) {
        long currentMills = System.currentTimeMillis();
        List<UserGroupBo> list = groupDao.userGroupList(userId, type, pageVO, new Date(currentMills));
        list.forEach(item -> {
            Integer status = SystemUtil.judgeGroupTrueStatus(currentMills, item.getEndTime().getTime(), item.getSurplusNum());
            item.setStatus(status);
        });
        return list;
    }

    @Override
    public Integer userGroupCount(long userId) {
        return groupMemberCache.userCurrentGroupCount(userId);
    }

    @Override
    public Long clearRedisCash(String redisKey) {
        return groupMemberCache.clearRedisCash(redisKey);
    }

    @Override
    public Long loadGroupByActivityCode(Long userId, String activityCode) {
        return groupDao.selectGroupIdByActivityCode(userId, activityCode);
    }

    @Override
    public Integer loadUserIsHasGroup(Long userId) {
        Integer groupId = groupMemberDao.selectGroupLastGroupByUserId(userId);
        return groupId == null ? BooleanEnum.False.getCode() : BooleanEnum.True.getCode();
    }

    @Override
    public GroupMsgBo loadGroupMessage(String groupCode, String orderNo) {
        Group group = groupCache.queryGroupByCache(groupCode);
        GroupMsgBo groupMsgBo = GroupMsgConverter.entity2Bo(group);
        AssertUtil.notNull(groupMsgBo, "团不存在");
        List<GroupMember> memberList = groupMemberCache.queryMemberListByCache(group.getId(), YesNoEnum.YES.getCode());
        AssertUtil.isTrue(CollectionUtils.isEmpty(memberList), "团成员不存在");
        memberList.forEach(member -> {
            if (Objects.equals(group.getHeadId(), member.getUserId())) {
                groupMsgBo.setHeadImg(member.getUserImg());
                groupMsgBo.setHeadNickname(member.getUsername());
            }
        });
        // 判断用户是否在团中
        if (StringUtils.isNotEmpty(orderNo)) {
            long size = memberList.stream().filter(item -> Objects.equals(item.getOrderNo(), orderNo)).count();
            groupMsgBo.setIsJoin(size == 0 ? BooleanEnum.False.getCode() : BooleanEnum.True.getCode());
        }
        return groupMsgBo;
    }

    @Override
    public GroupJoinStatusResp loadGroupJoinStatus(String groupCode, Long userId) {
        GroupJoinStatusResp resp = GroupJoinStatusResp.builder().build();
        Group group = groupCache.queryGroupByCache(groupCode);
        int joinStatus = SystemUtil.judgeGroupJoinStatus(group);
        resp.setJoinStatus(joinStatus);
        if (!(Objects.equals(joinStatus, NO_EXITS.getCode()) || Objects.equals(joinStatus, NO_START.getCode()))) {
            resp.setIsJoin(judgeUserIsJoinGroup(userId, group));
        }
        return resp;
    }

    // 判断用户是否加入团
    private Integer judgeUserIsJoinGroup(Long userId, Group group) {
        List<GroupMember> groupMemberList = groupMemberCache.queryMemberListByCache(group.getId(), BooleanEnum.True.getCode());
        long size = groupMemberList.stream().filter(item -> Objects.equals(item.getUserId(), userId)).count();
        return size == 0 ? BooleanEnum.False.getCode() : BooleanEnum.True.getCode();
    }

    @Override
    public ResultVO<String> createDraftGroup(Long userId, String activityCode, String spu) {
        ResultVO<String> result = ResultVO.newResult();
        Group group = new Group();
        //查询团信息
        FeignResult<ActivityTo> feignResult = activityFeignClient.queryActivityByActCode(activityCode);
        log.info("queryActivityByActCode, activityCode = {}", activityCode);
        if (feignResult.isFailure()) {
            log.error("queryActivityByActCode is fail, code = {}, message = {}", activityCode, feignResult.getMessage());
            return result.format(Boolean.FALSE, feignResult.getMessage());
        }
        ActivityTo activityTo = feignResult.getData();
        if (Objects.isNull(activityTo)) {
            log.error("activity is not exit, activityCode = {}", activityCode);
            return result.format(Boolean.FALSE, "此活动不存在");
        }
        //校验团信息
        verifyGroup(result, activityTo);
        if (!result.isRetBool()) {
            return result;
        }
        //生成唯一的groupCode
        String groupCode = GroupConstant.GROUP_CODE_PREFIX + ServerDefIdUtil.getInstance().getId() + IDGener.genOID();
        if (StringUtils.isBlank(groupCode)) {
            return result.format(Boolean.FALSE, "生成groupCode失败");
        }
        GroupRuleTo groupRuleTo = JSON.parseObject(activityTo.getActivityRule(), GroupRuleTo.class);
        log.info("createDraftGroup groupRuleTo={}",groupRuleTo.toString());

        initGroup(group, groupCode, activityCode, GroupTypeEnum.BUY_GROUP.getCode(), userId, groupRuleTo, null, null, spu);
        Integer saveCount = groupDao.insertSelective(group);
        if (!Objects.equals(saveCount, 1)) {
            return result.format(Boolean.FALSE, "创建草稿团失败");
        }
        return result.format(Boolean.TRUE, "创建草稿团成功", groupCode);
    }

    /**
     * 初始化团信息
     *
     * @param group        团实体
     * @param groupCode    团code
     * @param activityCode 活动code
     * @param groupType    活动类型
     * @param userId       用户id（团长id）
     * @param groupRuleTo  团规则
     */
    private void initGroup(Group group, String groupCode, String activityCode, Integer groupType, Long userId, GroupRuleTo groupRuleTo, Date groupStartDate, Date groupEndDate, String spu) {
        group.setGroupCode(groupCode);
        group.setActivityCode(activityCode);
        group.setGroupType(groupType);
        group.setHeadId(userId);
        group.setSpu(spu);
        group.setCreateTime(new Date(System.currentTimeMillis()));
        group.setUpdateTime(new Date(System.currentTimeMillis()));
        group.setSurplusNum(groupRuleTo.getGroupNumber());
        group.setLowerLimit(groupRuleTo.getGroupNumber());
        group.setUpperLimit(groupRuleTo.getMaxGroupNumber());
        group.setValidity(groupRuleTo.getValidity());
        group.setGroupRule(groupRuleTo.getGroupNumber() + "人团");
        group.setStartTime(groupStartDate);
        group.setEndTime(groupEndDate);
        group.setRemainingMsgPushStatus(0);
        if (Objects.equals(groupType, GroupTypeEnum.SHARE_GROUP.getCode())) {
            group.setJoinNum(1);
            group.setStatus(GroupStatusEnum.PROCESSING.getCode());
            group.setSurplusNum(groupRuleTo.getGroupNumber() - 1);
        } else {
            group.setJoinNum(0);
            group.setStatus(GroupStatusEnum.DRAFT.getCode());
            group.setSurplusNum(groupRuleTo.getGroupNumber());
        }
        group.setShopId(getShopId(userId));
    }

    private Long getShopId(Long userId) {
        log.info("userId={},initGroup init shopId",userId);
        User user = UserShopClient.getUser(userId);
        Long shopId = null;
        if(Objects.nonNull(user)){
            try {
                Stopwatch stopwatch = Stopwatch.createStarted();
                if(Objects.equals(UserTypeEnum.SHOPKEEPER.getCode(),user.getUserType().getCode())){
                    Shop shop = UserShopClient.getShopByOwnerId(userId);
                    shopId = Objects.nonNull(shop)?shop.getId():null;
                    log.info("getShopId userType is shopkeeper,userId={}, shopId={}",userId,shopId);
                }else{
                    Shop shop = UserShopClient.getShopByInviteCode(user.getFollowerInviteCode());
                    shopId = Objects.nonNull(shop)?shop.getId():null;
                    log.info("getShopId userType is fans,userId={}, shopId={}",userId,shopId);
                }
                DMonitor.recordOne("api_groupListByCondition_uc_shop", stopwatch.elapsed(TimeUnit.MILLISECONDS));
            } catch (Exception e) {
                log.error("getShopId error ,message="+e.getMessage(),e);
            }
        }
        return shopId;
    }


    /**
     * 校验团信息
     *
     * @param result
     * @param activityTo
     */
    private void verifyGroup(ResultVO result, ActivityTo activityTo) {
        result.format(Boolean.TRUE, "参团成功");
        log.info("activityTo info = {}", activityTo.toString());
        Integer activityType = activityTo.getActivityType();
        //8 - 拼团活动
        if (!Objects.equals(activityType, 8)) {
            result.format(Boolean.FALSE, "此活动不是拼团活动");
        } else {
            //1：新建；2：提报中；3：选品中；4：待生效；5：预热中；6：活动中；7：已结束；8：已作
            Integer activityStatus = activityTo.getActivityStatus();
            if (!Objects.equals(activityStatus, 6)) {
                result.format(Boolean.FALSE, "活动尚未开始");
            }
        }

    }


    @Override
    public ResultVO<ShareGroupTo> createShareGroup(GroupBo groupBo) {
        ResultVO<ShareGroupTo> result = ResultVO.newResult();
        ShareGroupTo shareGroupTo = ShareGroupTo.builder().build();
        //查询团信息
        FeignResult<ActivityTo> feignResult = activityFeignClient.queryActivityBySku(groupBo.getSku());
        if (feignResult.isFailure()) {
            log.error("queryActivityBySku is fail, sku = {}, message = {}", groupBo.getSku(), feignResult.getMessage());
            return result.format(Boolean.FALSE, "请求拼团活动失败");
        }
        ActivityTo activityTo = feignResult.getData();
        if (Objects.isNull(activityTo) || Strings.isNullOrEmpty(activityTo.getActivityCode())) {
            log.error("activity is not exit, sku = {}", groupBo.getSku());
            return result.format(Boolean.FALSE, "此活动不存在");
        }
        //校验团信息
        verifyGroup(result, activityTo);
        if (!result.isRetBool()) {
            return result.format(Boolean.FALSE, result.getMessage());
        }
        //加锁
        try (RLock rlock = LockHelper.getRLock(String.format(RedisKeyTemplate.PT_CREATE_SHARE_GROUP_KEY, activityTo.getActivityCode(), groupBo.getUserId(), groupBo.getSpu()))) {
            if (!rlock.tryLock(10, TimeUnit.SECONDS)) {
                log.error("create share group, get lock fail, activityCode = {}, userId = {}, spu = {}", activityTo.getActivityCode(), groupBo.getUserId(), groupBo.getSpu());
                return result.format(Boolean.FALSE, "用户创建分享团获取锁失败！");
            }
            Group group = groupDao.queryUniqShareGroup(activityTo.getActivityCode(), groupBo.getUserId(), groupBo.getSpu(), GroupTypeEnum.SHARE_GROUP.getCode(), GroupStatusEnum.PROCESSING.getCode());
            if (Objects.nonNull(group)) {
                shareGroupTo.setType(1);
                shareGroupTo.setGroupCode(group.getGroupCode());
                return result.format(Boolean.TRUE, "用户在此活动已创建了团", shareGroupTo);
            } else {
                group = new Group();
            }
            GroupRuleTo groupRuleTo = JSON.parseObject(activityTo.getActivityRule(), GroupRuleTo.class);
            log.info("createShareGroup groupRuleTo={}",groupRuleTo.toString());
            //生成唯一的groupCode
            String groupCode = GroupConstant.GROUP_CODE_PREFIX + ServerDefIdUtil.getInstance().getId() + IDGener.genOID();
            if (StringUtils.isBlank(groupCode)) {
                return result.format(Boolean.FALSE, "生成groupCode失败");
            }
            //初始化团信息
            initGroup(group, groupCode, activityTo.getActivityCode(), GroupTypeEnum.SHARE_GROUP.getCode(), groupBo.getUserId(), groupRuleTo, groupBo.getGroupStartDate(), groupBo.getGroupEndDate(), groupBo.getSpu());
            GroupMember groupMember = new GroupMember();
            getExtData(groupBo,group);
            initGroupMember(groupBo, group, null, groupMember, true);
            return groupTranService.createShareGroup(result, group, groupMember, groupBo, shareGroupTo, groupCode);
        } catch (Exception e) {
            log.error("创建分享团失败:", e);
            throw new GenericBusinessException(e.getMessage());
        }
    }


    @Override
    public GroupBo queryGroupDetail(UserDto userDto, String groupCode, String orderNo) {
        //1.团信息
        Group group = groupCache.queryGroupByCache(groupCode);
        if (Objects.isNull(group)) {
            log.info("queryGroupDetail groupCode = {},group info is null",groupCode);
            return null;
        } else {
            if (!Strings.isNullOrEmpty(orderNo)) {
                GroupMember groupMember = groupMemberDao.queryMember(group.getId(), orderNo);
                if (Objects.isNull(groupMember)) {
                    log.info("queryGroupDetail groupCode={},orderNo={},search groupMember is null",groupCode,orderNo);
                    return null;
                } else {
                    Integer isJoin = groupMember.getIsJoin();
                    if (Objects.equals(isJoin, YesNoEnum.NO.getCode())) {
                        log.info("queryGroupDetail groupCode={},orderNo={},search groupMember is null,join group failure",groupCode,orderNo);
                        return GroupBo.builder().isJoin(YesNoEnum.NO.getCode()).build();
                    }
                }
            }
        }
        //2.用户信息
        List<GroupMember> groupMembers = groupMemberCache.queryMemberListByCache(group.getId(), YesNoEnum.YES.getCode());
        if (CollectionUtils.isEmpty(groupMembers)) {
            log.info("queryGroupDetail groupMembers is null ,groupId={}",group.getId());
            return null;
        }
        //根据时间排序
        List<GroupMember> sortGroupMembers = groupMembers.stream().sorted(Comparator.comparing(GroupMember::getCreateTime)).collect(Collectors.toList());
        //获取团长信息
        Optional<GroupMember> headOptionl = sortGroupMembers.stream().findFirst();
        if (!headOptionl.isPresent()) {
            log.info("queryGroupDetail headOptionl is null ,groupId={}",group.getId());
            return null;
        }
        GroupMember head = headOptionl.get();
        //通过xc_sale获取商品详情
        FeignResponse<GoodGroupDetailTo> feignResponse = goodDetailFeignClient.queryGoodGroupDetail(head.getSku());
        if (feignResponse.isFailure()) {
            log.error("queryGoodGroupDetail is fail, sku = {}, message = {}", head.getSku(), feignResponse.getErrorMsg());
            throw new GenericBusinessException(feignResponse.getErrorMsg());
        }
        GoodGroupDetailTo goodGroupDetailTo = feignResponse.getData();
        if (Objects.isNull(goodGroupDetailTo)) {
            log.error("goodGroupDetail is null, sku = {}", head.getSku());
            throw new GenericBusinessException("拼团商品详情为空");
        }
        //3.获取标签类型
        List<String> tags = getTags(goodGroupDetailTo, group.getGroupRule(), userDto.getId(), head.getSku());
        //获取skus
        List<String> skus = goodGroupDetailTo.getSkus();
        if (!skus.contains(head.getSku())) {
            skus.add(head.getSku());
        }
        //4. 获取用户评价
        List<EvaluationBo> evaluations = getEvaluations(skus);
        //5. 获取虚拟商品
        Integer isExistsVirtual = getVirtualGood(head.getSku());
        //6. VIP用户是否已经登录过app
        Boolean vipLogined = isVipLogined(userDto.getId());
        //获取团价格信息
        PriceTo priceTo = getPrice(head.getSku());
        //4.构建
        return buildBo(priceTo,group, sortGroupMembers, head, userDto.getId(), userDto.getUserType().getCode(), tags, evaluations, skus, isExistsVirtual, vipLogined, goodGroupDetailTo.getGoodsShowName());
    }

    /**
     * 判断用户是否登录过
     *
     * @param userId
     * @return
     */
    public Boolean isVipLogined(Long userId) {
        ApiResult<UserLoginDevice> loginResult = UserShopClient.firstAppLoginInfo(userId);
        Boolean loginedFlag = Boolean.FALSE;
        if (loginResult.isRetBool() && Objects.nonNull(loginResult.getT())) {
            loginedFlag = Boolean.TRUE;
        }
        return loginedFlag;
    }

    /**
     * 通过sku 获取虚拟商品
     *
     * @param sku
     * @return
     */
    public Integer getVirtualGood(String sku) {
        FeignResult<VirtualGoodTo> feignResult = virtualGoodFeignClient.queryVirtualGood(sku);
        if (feignResult.isSuccess() && Objects.nonNull(feignResult.getData())) {
            return YesNoEnum.YES.getCode();
        } else {
            return YesNoEnum.NO.getCode();
        }
    }

    /**
     * 获取指定sku的spu下面的所有参加活动的skus
     *
     * @param goodDetailTo 商品详情
     * @return
     */
    public List<String> getSkus(GoodDetailTo goodDetailTo) {
        List<String> skus = Lists.newArrayList();
        JSONObject treeInfo = goodDetailTo.getTreeInfo();
        if (Objects.isNull(treeInfo)) {
            skus.add(goodDetailTo.getSku());
            return skus;
        }
        Map<String, Object> treeSelect = (Map<String, Object>) treeInfo.get("tree_select");
        getSkus(treeSelect, skus);
        return skus;
    }

    /**
     * 获取skus
     *
     * @param treeSelect treeSelect
     * @param skus       skus
     */
    private void getSkus(Map<String, Object> treeSelect, List<String> skus) {
        if (treeSelect.containsKey("sku")) {
            skus.add((String) treeSelect.get("sku"));
        } else {
            Set<String> keySet = treeSelect.keySet();
            keySet.forEach(key -> {
                getSkus((Map<String, Object>) treeSelect.get(key), skus);
            });
        }
    }

    /**
     * 调用xc_sale获取商品标签
     *
     * @param goodGroupDetailTo 拼团商品详情
     * @param groupRule    团规则 2人团
     * @return
     */
    public List<String> getTags(GoodGroupDetailTo goodGroupDetailTo, String groupRule, Long userId, String sku) {
        //3.查询标签信息
        List<String> tags = Lists.newArrayList();
        tags.add(groupRule);
        Integer isInTimeline = goodGroupDetailTo.getIsInTimeline();
        //YES
        if (Objects.equals(isInTimeline, YesNoEnum.YES.getCode())) {
            tags.add(GroupTagEnum.SUP_SALE.getName());
        }
        getCouponTag(goodGroupDetailTo.getGoodsId(), sku, userId, tags);
        //Y
        String preSaleYn = goodGroupDetailTo.getPreSaleYn();
        if (Objects.equals(preSaleYn, "Y")) {
            tags.add(GroupTagEnum.PRE_SALE.getName());
        }
        int isForeignSupply = goodGroupDetailTo.getIsForeignSupply();
        int isInBond = goodGroupDetailTo.getIsInBond();
        //2 - 海外直邮
        if (isForeignSupply == 2) {
            tags.add(GroupTagEnum.OVERSEAS_DIRECT_MAIL.getName());
        } else {
            if (isInBond == 1) {
                tags.add(GroupTagEnum.IN_BOND.getName());
            }
        }
        return tags;
    }

    /**
     * 获取可用券的信息
     *
     * @param goodsId
     * @param sku
     * @param tags
     */
    private void getCouponTag(Long goodsId, String sku, Long userId, List<String> tags) {
        //获取可用券
        ProductAo productAo = ProductAo.builder().build();
        List<ProductAo> products = Lists.newArrayList();
        productAo.setSku(sku);
        productAo.setGoodId(goodsId);
        //查询品牌
        Map<String, Object> goodMap = getGoodInfo(sku);
        Long brandId = goodMap.get("brand_id") == null ? null : Long.valueOf(goodMap.get("brand_id").toString());
        productAo.setBrandId(brandId);

        products.add(productAo);
        String productJsonStr = JSON.toJSONString(products);
        FeignResponse<Map<String, Integer>> batchCoupon = couponFeignClient.findOptimalBatchCoupon(userId, productJsonStr);
        if (batchCoupon.isFailure()) {
            log.error("findOptimalBatchCoupon is fail, userId = {}, productJsonStr = {}, message = {}", batchCoupon.getErrorMsg());
            throw new GenericBusinessException(batchCoupon.getErrorMsg());
        }
        Map<String, Integer> skuMap = batchCoupon.getData();
        if (MapUtils.isEmpty(skuMap)) {
            return;
        }
        log.info("skuMap info = {}", skuMap.toString());
        Integer status = skuMap.get(sku);
        if (Objects.equals(status, YesNoEnum.YES.getCode())) {
            tags.add(GroupTagEnum.USE_COUPON.getName());
        }
    }

    /**
     * 获取商品信息
     *
     * @param sku
     * @return
     */
    private Map<String, Object> getGoodInfo(String sku) {
        long goodInfoStart = System.currentTimeMillis();
        FeignResponse<Map<String, Object>> feignResponse = goodInfoFeignClient.queryGoodInfo("xc", sku);
        long goodInfoEnd = System.currentTimeMillis();
        log.info("goodInfo time = {}", goodInfoEnd - goodInfoStart);
        if (feignResponse.isFailure()) {
            log.error("queryGoodInfo id fail, sku = {}, message = {}", sku, feignResponse.getErrorMsg());
            throw new GenericBusinessException(feignResponse.getErrorMsg());
        }
        Map<String, Object> goodMap = feignResponse.getData();
        if (CollectionUtils.isEmpty(goodMap)) {
            log.error("goodMap is empty, sku = {}", sku);
            throw new GenericBusinessException("商品信息为空");
        }
        log.info("goodMap = {}", goodMap.toString());
        return goodMap;
    }

    /**
     * 调用客服中心 获取用户评价信息
     *
     * @param skus
     * @return
     */
    public List<EvaluationBo> getEvaluations(List<String> skus) {
        //调用客服中心获取评价信息
        List<EvaluationBo> evaluationBos = Lists.newArrayList();
        String sku = String.join(",", skus);
        EvaluationAo evaluationAo = new EvaluationAo();
        evaluationAo.setSku(sku);
        FeignResponse<List<EvaluationTo>> feignResp = evaluationsFeignClient.queryEvaList(evaluationAo);
        if (feignResp.isFailure()) {
            log.error("queryEvaList is fail, evaluationAo = {}, message = {}", evaluationAo.toString(), feignResp.getErrorMsg());
            throw new GenericBusinessException(feignResp.getErrorMsg());
        }
        List<EvaluationTo> evaluationTos = feignResp.getData();
        if (CollectionUtils.isEmpty(evaluationTos)) {
            return evaluationBos;
        }
        evaluationTos.forEach(evaluationTo -> {
            EvaluationBo evaluationBo = new EvaluationBo();
            evaluationBo.setImage(evaluationTo.getUserLogo());
            evaluationBo.setContent(evaluationTo.getContent());
            evaluationBos.add(evaluationBo);
        });
        return evaluationBos;
    }

    /**
     * 构建详情信息
     *
     * @param group
     * @param sortGroupMembers
     * @param head
     * @param userId
     * @param userType
     * @param tags
     * @param evaluations
     * @param skus
     * @param isExistsVirtual
     * @param vipLogined
     * @return
     */
    private GroupBo buildBo(PriceTo priceTo,Group group, List<GroupMember> sortGroupMembers, GroupMember head, Long userId, Integer userType, List<String> tags, List<EvaluationBo> evaluations, List<String> skus, Integer isExistsVirtual, Boolean vipLogined, String goodShowName) {
        GroupBo groupBo = GroupBo.builder().build();
        //团信息
        groupBo.setGroupType(group.getGroupType());
        groupBo.setLowerLimit(group.getLowerLimit());
        groupBo.setUpperLimit(group.getUpperLimit());
        groupBo.setSurplusCount(group.getSurplusNum());
        groupBo.setGroupStatus(group.getStatus());
        groupBo.setJoinNum(group.getJoinNum());
        Date groupEndTime = group.getEndTime();
        groupBo.setGroupEndDate(groupEndTime);
        groupBo.setGroupCode(group.getGroupCode());
        groupBo.setShortTitle(priceTo.getShortTitle());
        //剩余时间
        long surplusTime = groupEndTime.getTime() - System.currentTimeMillis();
        if (surplusTime > 0L) {
            groupBo.setGroupSurplusTime(surplusTime);
        } else {
            groupBo.setGroupSurplusTime(0L);
        }
        //当前用户是否加入该团
        Map<Long, GroupMember> longGroupMemberMap = sortGroupMembers.stream().collect(Collectors.toMap(GroupMember::getUserId, Function.identity()));
        if (longGroupMemberMap.containsKey(userId)) {
            groupBo.setCurrWithJoin(BooleanEnum.True.getCode());
            GroupMember currGroupMember = longGroupMemberMap.get(userId);
            groupBo.setOrderStatus(currGroupMember.getOrderStatus());
            groupBo.setOrderNo(currGroupMember.getOrderNo());
        } else {
            groupBo.setCurrWithJoin(BooleanEnum.False.getCode());
        }

        //商品信息
        groupBo.setProductImg(head.getProductImg());
        groupBo.setProductName(head.getProductName());
        groupBo.setProductTitle(goodShowName);
        groupBo.setPositivePrice(head.getPositivePrice());
        groupBo.setSalePrice(head.getSalePrice());
        groupBo.setSku(head.getSku());
        //用户信息
        List<GroupUserBo> groupUserBos = Lists.newArrayList();
        sortGroupMembers.forEach(sortGroupMember -> {
            GroupUserBo groupUserBo = new GroupUserBo();
            groupUserBo.setUserId(sortGroupMember.getUserId());
            groupUserBo.setImage(sortGroupMember.getUserImg());
            groupUserBo.setNickName(sortGroupMember.getUsername());
            groupUserBo.setIsNew(sortGroupMember.getIsNew());
            groupUserBo.setUserRole(sortGroupMember.getGroupRole());
            groupUserBos.add(groupUserBo);
        });
        groupBo.setGroupUserBos(groupUserBos);
        List<GroupUserBo> currUserList = groupUserBos.stream().filter(groupUserBo -> Objects.equals(groupUserBo.getUserId(), userId)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(currUserList)) {
            groupBo.setCurrUserRole(GroupRoleEnum.MEMBER.getCode());
        } else {
            groupBo.setCurrUserRole(currUserList.get(0).getUserRole());
        }
        groupBo.setEvaluationBos(evaluations);
        groupBo.setTags(tags);
        if (skus.size() > 1) {
            groupBo.setIsMulNormal(YesNoEnum.YES.getCode());
        } else {
            groupBo.setIsMulNormal(YesNoEnum.NO.getCode());
        }
        if (Objects.equals(userType, UserTypeEnum.VIP.getCode()) && vipLogined) {
            groupBo.setIsVipLogined(YesNoEnum.YES.getCode());
        } else {
            groupBo.setIsVipLogined(YesNoEnum.NO.getCode());
        }
        groupBo.setIsExistsVirtual(isExistsVirtual);
        groupBo.setIsJoin(YesNoEnum.YES.getCode());
        return groupBo;
    }

    @Override
    public Group getGroupByCode(String groupCode) {
        return groupCache.queryGroupByCache(groupCode);
    }

    @Override
    public void joinGroup(GroupBo groupBo) {
        GroupMember failGroupMember = new GroupMember();
        try (RLock rlock = LockHelper.getRLock(String.format(RedisKeyTemplate.PT_JOIN_GROUP_KEY, groupBo.getGroupCode()))) {
            if (!rlock.tryLock(10, TimeUnit.SECONDS)) {
                Group group = groupCache.queryGroupByCache(groupBo.getGroupCode());
                buildFailMember(failGroupMember, groupBo, group);
                log.error("join group, get lock fail, userId = {}, groupCode = {}", groupBo.getUserId(), groupBo.getGroupCode());
                //发起退款  发送消息
                groupMemberDao.insertSelective(failGroupMember);
                if (!Objects.equals(groupBo.getRefundYn(), YesNoEnum.YES.getCode())) {
                    groupSend.refundSend(groupBo.getOrderNo());
                }
                return;
            }
            //通过groupCode查询Group
            //团是否存在
            Group group = groupCache.queryGroupByCache(groupBo.getGroupCode());
            if (Objects.isNull(group)) {
                log.error("group is not exit, groupCode = {}", groupBo.getGroupCode());
                //发起退款  发送消息
                groupSend.refundSend(groupBo.getOrderNo());
                return;
            }
            //校验消息
            GroupMember groupMemberByOrder = groupMemberDao.queryMember(group.getId(), groupBo.getOrderNo());
            if (Objects.nonNull(groupMemberByOrder)) {
                log.info("groupMemberByOrder info = {}", groupMemberByOrder.toString());
                return;
            }
            buildFailMember(failGroupMember, groupBo, group);
            if (Objects.equals(groupBo.getRefundYn(), YesNoEnum.YES.getCode())) {
                log.info("join group Intercept, has refund, groupCode = {}, orderNo = {}", groupBo.getGroupCode(), groupBo.getOrderNo());
                groupMemberDao.insertSelective(failGroupMember);
                return;
            }
            //团是否已经结束或者团人数是否已经到达上限
            Integer groupStatus = group.getStatus();
            Date groupEndTime = group.getEndTime();
            if (Objects.nonNull(groupEndTime)) {
                if (System.currentTimeMillis() > groupEndTime.getTime() || Objects.equals(group.getUpperLimit(), group.getJoinNum())) {
                    log.error("group has end or has upperLimit, groupCode = {}", groupBo.getGroupCode());
                    //发起退款  发送消息
                    groupSend.refundSend(groupBo.getOrderNo());
                    groupMemberDao.insertSelective(failGroupMember);
                    return;
                }
            }

            //用户信息
            List<GroupMember> groupMembers = groupMemberCache.queryMemberListByCache(group.getId(), YesNoEnum.YES.getCode());
            List<Long> userIds = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(groupMembers)) {
                userIds = groupMembers.stream().map(GroupMember::getUserId).collect(Collectors.toList());
                if (userIds.contains(groupBo.getUserId())) {
                    log.error("user has join this group = {}", groupBo.getGroupCode());
                    //发起退款  发送消息
                    groupSend.refundSend(groupBo.getOrderNo());
                    groupMemberDao.insertSelective(failGroupMember);
                    return;
                }
            }
            //判断团的状态
            if (!Objects.equals(group.getStatus(), GroupStatusEnum.DRAFT.getCode()) &&
                    Objects.equals(SystemUtil.judgeGroupTrueStatus(System.currentTimeMillis(), group.getEndTime().getTime(), group.getSurplusNum()), GroupStatusEnum.FAIL.getCode())) {
                log.error("group already fail, groupCode = {}", group.getGroupCode());
                //发起退款  发送消息
                groupSend.refundSend(groupBo.getOrderNo());
                groupMemberDao.insertSelective(failGroupMember);
                return;
            }
            group.setJoinNum(group.getJoinNum() + 1);
            group.setSurplusNum(group.getSurplusNum() - 1 <= 0 ? 0 : group.getSurplusNum() - 1);
            group.setRemainingMsgPushStatus(0);
            GroupMember groupMember = new GroupMember();
            if (Objects.equals(groupStatus, GroupStatusEnum.DRAFT.getCode()) && Objects.equals(group.getHeadId(), groupBo.getUserId())) {
                group.setStatus(GroupStatusEnum.PROCESSING.getCode());
                groupMember.setGroupRole(GroupRoleEnum.HEAD.getCode());
                group.setStartTime(groupBo.getGroupStartDate());
                group.setEndTime(groupBo.getGroupEndDate());
            }
            getExtData(groupBo,group);
            //初始化团信息
            boolean groupSuccess = initGroupMember(groupBo, group, groupStatus, groupMember, false);
            //事物性执行插入操作
            groupTranService.joinGroup(groupMember, groupSuccess, userIds, groupBo, group, groupMembers);
        } catch (Exception e) {
            log.error("join group fail", e);
            log.info("insert fail member info = {}", failGroupMember.toString());
            throw new GenericBusinessException(e.getMessage());
        }
    }

    /**
     * 构建参团失败成员信息
     *
     * @param failGroupMember
     * @param groupBo
     */
    private void buildFailMember(GroupMember failGroupMember, GroupBo groupBo, Group group) {
        failGroupMember.setUserId(groupBo.getUserId());
        failGroupMember.setUserImg(StringUtils.EMPTY);
        failGroupMember.setUsername(StringUtils.EMPTY);
        failGroupMember.setGroupId(group.getId());
        failGroupMember.setSku(groupBo.getSku());
        failGroupMember.setProductName(StringUtils.EMPTY);
        failGroupMember.setProductImg(StringUtils.EMPTY);
        failGroupMember.setPositivePrice(BigDecimal.ZERO);
        failGroupMember.setSalePrice(BigDecimal.ZERO);
        failGroupMember.setTotalMoney(BigDecimal.ZERO);
        failGroupMember.setOrderStatus(GroupOrderStatusEnum.PAY_SUCCESS.getCode());
        failGroupMember.setJoinType(GroupTypeEnum.BUY_GROUP.getCode());
        failGroupMember.setGroupRole(YesNoEnum.NO.getCode());
        failGroupMember.setOrderNo(groupBo.getOrderNo());
        failGroupMember.setBuyNum(groupBo.getBuyCount());
        failGroupMember.setIsNew(YesNoEnum.NO.getCode());
        failGroupMember.setValidFlag(YesNoEnum.YES.getCode());
        failGroupMember.setRemindMesFlag(YesNoEnum.NO.getCode());
        failGroupMember.setIsJoin(YesNoEnum.NO.getCode());
        failGroupMember.setCreateTime(new Date(System.currentTimeMillis()));
        failGroupMember.setUpdateTime(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取外部数据
     *
     * @param groupBo
     */
    private void getExtData(GroupBo groupBo,Group group) {
        //获取用户信息
        FeignResponse<UserVO> feignResponse = uCenterFeignClient.getUserById(groupBo.getUserId());
        if (feignResponse.isFailure()) {
            log.error("getUserById id fail, userId = {}, message = {}", groupBo.getUserId(), feignResponse.getErrorMsg());
            throw new GenericBusinessException(feignResponse.getErrorMsg());
        }
        UserVO userVO = feignResponse.getData();
        if (Objects.isNull(userVO)) {
            log.error("user is not exit, userId = {}", groupBo.getUserId());
            throw new GenericBusinessException("用户不存在");
        }
        if (!Strings.isNullOrEmpty(userVO.getNickName())) {
            groupBo.setNickName(userVO.getNickName());
        } else {
            if (!Strings.isNullOrEmpty(userVO.getMobile())) {
                groupBo.setNickName(StringXcUtils.hidePhoneNum(userVO.getMobile()));
            } else {
                groupBo.setNickName(SystemConstant.DEFAULT_USER_NICK_NAME);
            }
        }
        if (!Strings.isNullOrEmpty(userVO.getHeadimgUrl())) {
            groupBo.setUserImage(userVO.getHeadimgUrl());
        } else {
            groupBo.setUserImage(SystemConstant.DEFAULT_USER_HEAD_URL);
        }

        //获取团价格信息
        PriceTo priceRedisModel = getPriceTo(groupBo.getSku());
        groupBo.setSalePrice(BigDecimal.valueOf(priceRedisModel.getSales()));

        //获取商品一般价格信息
        PriceAo priceAo = new PriceAo();
        priceAo.setSku(groupBo.getSku());
        priceAo.setPriceType(1);
        FeignResponse<PriceTo> priceToFeignResponse = priceFeignClient.qureyPriceCur(priceAo);
        if (priceToFeignResponse.isFailure()) {
            log.error("qureyPriceCur is fail, priceAo = {}, message = {}", priceAo.toString(), priceToFeignResponse.getErrorMsg());
            throw new GenericBusinessException(priceToFeignResponse.getErrorMsg());
        }
        PriceTo priceTo = priceToFeignResponse.getData();
        if (Objects.isNull(priceTo)) {
            log.error("priceTo is null, priceAo = {}", priceAo.toString());
            throw new GenericBusinessException("一般价格为空");
        }
        groupBo.setPositivePrice(BigDecimal.valueOf(priceTo.getSales()));

        //获取商品信息
        Map<String, Object> goodMap = getGoodInfo(groupBo.getSku());
        groupBo.setProductName(goodMap.get("goods_name").toString());
        groupBo.setProductImg(goodMap.get("image").toString());
    }

    /**
     * 获取团价格信息
     * @param sku
     * @return
     */
    private PriceTo getPriceTo(String sku) {
        FeignResult<ActivityTo> feignResult = activityFeignClient.queryActivityBySku(sku);
        if (feignResult.isFailure()) {
            log.error("queryActivityBySku is fail, sku = {}, message = {}", sku, feignResult.getMessage());
            throw new GenericBusinessException("请求拼活动失败");
        }
        ActivityTo activityTo = feignResult.getData();
        if (Objects.isNull(activityTo) || Strings.isNullOrEmpty(activityTo.getActivityCode())) {
            log.error("activity is not exit, sku = {}", sku);
            throw new GenericBusinessException("此活动不存在");
        }
        log.info("activityTo info = {}", activityTo.toString());
        return activityTo.getPriceRedisModel();
    }
    /**
     * 获取团价格信息
     * @param sku
     * @return
     */
    private PriceTo getPrice(String sku) {
        PriceTo priceTo;
        try {
            priceTo = activityFeignClient.queryActivityBySku(sku).getData().getPriceRedisModel();
        } catch (Exception e) {
            priceTo = new PriceTo();
        }
        return priceTo;
    }

    /**
     * 构建 团成员信息
     *
     * @param groupBo
     * @param group
     * @param groupStatus
     * @param groupMember
     * @param createFlag  true - 创建分享团初始化团长信息  false - 参团，添加团员信息
     * @return
     */
    private boolean initGroupMember(GroupBo groupBo, Group group, Integer groupStatus, GroupMember groupMember, boolean createFlag) {
        boolean groupSuccess = Boolean.FALSE;
        if (createFlag) {
            groupMember.setGroupRole(GroupStatusEnum.PROCESSING.getCode());
            groupMember.setGroupRole(GroupRoleEnum.HEAD.getCode());
            groupMember.setIsNew(YesNoEnum.NO.getCode());
            groupMember.setJoinType(GroupTypeEnum.SHARE_GROUP.getCode());
            groupMember.setOrderNo(StringUtils.EMPTY);
            groupMember.setBuyNum(0);
            groupMember.setTotalMoney(BigDecimal.ONE);
        } else {
            if (Objects.equals(groupStatus, GroupStatusEnum.PROCESSING.getCode())) {
                if (Objects.equals(group.getJoinNum(), group.getLowerLimit())) {
                    group.setStatus(GroupStatusEnum.SUCCESS.getCode());
                    groupSuccess = Boolean.TRUE;
                }
                groupMember.setGroupRole(GroupRoleEnum.MEMBER.getCode());
            }
            if (Objects.equals(groupStatus, GroupStatusEnum.SUCCESS.getCode())) {
                groupMember.setGroupRole(GroupRoleEnum.INTRUDER.getCode());
            }
            if (Objects.equals(groupBo.getUserRole(), VisibleTypeEnum.VipNew.getCode())) {
                groupMember.setIsNew(YesNoEnum.YES.getCode());
            } else {
                groupMember.setIsNew(YesNoEnum.NO.getCode());
            }
            groupMember.setJoinType(GroupTypeEnum.BUY_GROUP.getCode());
        }
        //构建GroupMember
        groupMember.setUserId(groupBo.getUserId());
        groupMember.setUserImg(groupBo.getUserImage());
        groupMember.setUsername(groupBo.getNickName());
        groupMember.setGroupId(group.getId());
        groupMember.setSku(groupBo.getSku());
        groupMember.setProductName(groupBo.getProductName());
        groupMember.setProductImg(groupBo.getProductImg());
        groupMember.setPositivePrice(groupBo.getPositivePrice());
        groupMember.setSalePrice(groupBo.getSalePrice());
        groupMember.setTotalMoney(groupBo.getTotalMoney());
        groupMember.setOrderStatus(GroupOrderStatusEnum.PAY_SUCCESS.getCode());
        groupMember.setOrderNo(groupBo.getOrderNo());
        groupMember.setBuyNum(groupBo.getBuyCount());
        groupMember.setIsJoin(YesNoEnum.YES.getCode());
        groupMember.setCreateTime(new Date(System.currentTimeMillis()));
        groupMember.setUpdateTime(new Date(System.currentTimeMillis()));
        return groupSuccess;
    }
}
