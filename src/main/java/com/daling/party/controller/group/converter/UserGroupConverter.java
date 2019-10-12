package com.daling.party.controller.group.converter;

import com.daling.party.controller.group.resp.UserGroupListResp;
import com.daling.party.infrastructure.utils.AssertUtil;
import com.daling.party.service.group.bo.UserGroupBo;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by lilindong on 2019/4/8.
 */
public class UserGroupConverter {

    private UserGroupConverter() {}

    public static UserGroupListResp bo2UserGroupListResp(UserGroupBo userGroupBo) {
        AssertUtil.notNull(userGroupBo, "bo is null");
        UserGroupListResp resp = UserGroupListResp.builder()
                .groupCode(userGroupBo.getGroupCode()).status(userGroupBo.getStatus())
                .groupRule(userGroupBo.getGroupRule()).productSku(userGroupBo.getProductSku()).productName(userGroupBo.getProductName())
                .productImg(userGroupBo.getProductImg()).productPositivePerice(userGroupBo.getProductPositivePerice())
                .productSalePerice(userGroupBo.getProductSalePerice()).joinTime(userGroupBo.getJoinTime())
                .build();

        // 拼团剩余时间
        if (userGroupBo.getEndTime() != null && userGroupBo.getEndTime().getTime() > System.currentTimeMillis()) {
            resp.setSurplusTime(userGroupBo.getEndTime().getTime() - System.currentTimeMillis());
        }
        return resp;
    }


    public static List<UserGroupListResp> bo2UserGroupListResp(List<UserGroupBo> boList) {
        List<UserGroupListResp> respList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(boList)) {
            boList.forEach(item -> respList.add(UserGroupConverter.bo2UserGroupListResp(item)));
        }
        return respList;
    }
}
