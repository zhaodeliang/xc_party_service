package com.daling.party.controller.group.converter;

import com.daling.party.controller.group.req.JoinGroupRequest;
import com.daling.party.controller.group.req.ShareGroupRequest;
import com.daling.party.controller.group.resp.GroupDetailResp;
import com.daling.party.infrastructure.utils.DateTimeTool;
import com.daling.party.service.group.bo.GroupBo;
import org.testng.collections.Lists;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author jiwei.xue
 * @date 2019/4/9 18:30
 */
public class GroupConverter {

    public static GroupBo req2Bo(ShareGroupRequest shareGroupRequest) {
        if (Objects.isNull(shareGroupRequest)) {
            return null;
        }
        return GroupBo.builder()
                .groupEndDate(new Date(Long.valueOf(shareGroupRequest.getGroupEndDate())))
                .groupStartDate(new Date(Long.valueOf(shareGroupRequest.getGroupStartDate())))
                .sku(shareGroupRequest.getSku())
                .spu(shareGroupRequest.getSpu())
                .userId(Long.valueOf(shareGroupRequest.getUserId()))
                .userRole(Integer.valueOf(shareGroupRequest.getUserRole()))
                .build();
    }

    public static GroupBo req2Bo(JoinGroupRequest joinGroupRequest) {
        if (Objects.isNull(joinGroupRequest)) {
            return null;
        }
        return GroupBo.builder()
                .sku(joinGroupRequest.getSku())
                .buyCount(joinGroupRequest.getBuyCount())
                .totalMoney(BigDecimal.valueOf(Double.valueOf(joinGroupRequest.getTotalMoney())))
                .groupStartDate(DateTimeTool.parse(joinGroupRequest.getGroupStartDate(), DateTimeTool.strDateTimeFormat))
                .groupEndDate(DateTimeTool.parse(joinGroupRequest.getGroupEndDate(), DateTimeTool.strDateTimeFormat))
                .groupCode(joinGroupRequest.getGroupCode())
                .userRole(joinGroupRequest.getUserRole())
                .userId(joinGroupRequest.getUserId())
                .orderNo(joinGroupRequest.getOrderNo())
                .orderStatus(joinGroupRequest.getOrderStatus())
                .build();
    }

    public static GroupDetailResp bo2Resp(GroupBo detailBo) {
        if (Objects.isNull(detailBo)) {
            return null;
        }

        GroupDetailResp.Group group = GroupDetailResp.Group.builder()
                .currWithJoin(detailBo.getCurrWithJoin())
                .groupEndDate(DateTimeTool.CSTFormat(detailBo.getGroupEndDate().toString()))
                .groupSurplusTime(detailBo.getGroupSurplusTime())
                .groupType(detailBo.getGroupType())
                .groupStatus(detailBo.getGroupStatus())
                .joinNum(detailBo.getJoinNum())
                .lowerLimit(detailBo.getLowerLimit())
                .surplusCount(detailBo.getSurplusCount())
                .upperLimit(detailBo.getUpperLimit())
                .groupCode(detailBo.getGroupCode())
                .build();



        GroupDetailResp.Product product = GroupDetailResp.Product.builder()
                .productImg(detailBo.getProductImg())
                .productName(detailBo.getProductName())
                .productTitle(detailBo.getProductTitle())
                .lowerLimit(detailBo.getLowerLimit())
                .positivePrice(detailBo.getPositivePrice().toString())
                .salePrice(detailBo.getSalePrice().toString())
                .sku(detailBo.getSku())
                .isMulNormal(detailBo.getIsMulNormal())
                .tags(detailBo.getTags())
                .shortTitle(detailBo.getShortTitle())
                .build();

        List<GroupDetailResp.User> users = Lists.newArrayList();
        detailBo.getGroupUserBos().forEach(groupUserBo -> {
            GroupDetailResp.User user = GroupDetailResp.User.builder()
                    .userId(groupUserBo.getUserId())
                    .image(groupUserBo.getImage())
                    .isNew(groupUserBo.getIsNew())
                    .nickName(groupUserBo.getNickName())
                    .userRole(groupUserBo.getUserRole())
                    .build();
            users.add(user);
        });

        List<GroupDetailResp.Evaluation> evaluations = Lists.newArrayList();
        detailBo.getEvaluationBos().forEach(evaluationBo -> {
            GroupDetailResp.Evaluation evaluation = GroupDetailResp.Evaluation.builder()
                    .image(evaluationBo.getImage())
                    .content(evaluationBo.getContent())
                    .build();
            evaluations.add(evaluation);
        });

        return GroupDetailResp.builder()
                .currUserRole(detailBo.getCurrUserRole())
                .friendNum(detailBo.getFriendNum())
                .product(product)
                .group(group)
                .users(users)
                .evaluations(evaluations)
                .orderStatus(detailBo.getOrderStatus())
                .orderNo(detailBo.getOrderNo())
                .isVipLogined(detailBo.getIsVipLogined())
                .isExistsVirtual(detailBo.getIsExistsVirtual())
                .build();
    }
}
