package com.daling.party.controller.group.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lilindong on 2019/4/8.
 * 拼团详情
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMsgResp implements Serializable {
    private String groupCode; // 团编码
    private Integer status; // 拼团状态 1：成功 2：失败 3：进行中
    private Integer groupType; // 1、分享团 2、自购团
    private String groupRule; // 团规则 例：2人团
    private Integer upperLimit; // 成团人数上限
    private Integer lowerLimit; // 成团人数下限
    private Integer surplusNum; // 成团剩余数量
    private Integer joinNum; // 参团数量
    private Date startTime; // 拼团开始时间
    private Date endTime; // 拼团结束时间
    private Long headUserId; // 团长Id
    private String headImg; // 团长头像
    private String headNickname; // 团长昵称
    private Date createTime; // 团创建时间
    private Long surplusTime; // 拼团剩余时间（单位秒）
    private Integer isJoin; // 用户是否在团中
}

