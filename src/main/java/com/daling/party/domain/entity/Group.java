package com.daling.party.domain.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "s_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //团编码
    private String groupCode;
    //团长用户ID
    private Long headId;
    //spu
    private String spu;
    //活动ID-来自活动中心
    private String activityCode;
    //团类型(1、分享团2、自购团)
    private Integer groupType;
    //团规则 (ex 2人团)
    private String groupRule;
    //团人数上限
    private Integer upperLimit;
    //团人数下限
    private Integer lowerLimit;
    //活动开始时间
    private Date startTime;
    //活动结束时间
    private Date endTime;
    //团状态(0:草稿1：成功 2：失败 3：进行中(失败的状态为定时任务更新，不实时，增加判断))
    private Integer status;
    //参团数量
    private Integer joinNum;
    //剩余的数量（lower_limit每次参团-1直到为0）
    private Integer surplusNum;
    //即将结束的拼团推送消息状态(1:已推送0:未推送)
    private Integer remainingMsgPushStatus;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //当前活动的时长
    private Integer validity;
    //店铺id
    private Long shopId;
}
