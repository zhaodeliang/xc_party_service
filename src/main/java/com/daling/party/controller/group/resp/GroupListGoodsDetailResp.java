package com.daling.party.controller.group.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by lilindong on 2019/4/8.
 * 拼团详情
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupListGoodsDetailResp implements Serializable {
    private String groupCode; // 团编码
    private Integer surplusNum; // 成团剩余数量
    private Long headUserId; // 团长Id
    private String headImg; // 团长头像
    private String headNickname; // 团长昵称

}

