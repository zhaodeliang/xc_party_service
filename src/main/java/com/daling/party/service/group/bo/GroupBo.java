package com.daling.party.service.group.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jiwei.xue
 * @date 2019/4/10 12:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupBo {

    /**
     * 团code
     */
    private String groupCode;

    /**
     * 活动code
     */
    private String activityCode;

    /**
     * sku
     */
    private String sku;

    /**
     * spu
     */
    private String spu;

    /**
     * 团开始时间
     */
    private Date groupStartDate;

    /**
     * 团结束时间
     */
    private Date groupEndDate;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户角色 是否是新用户
     */
    private Integer userRole;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 商品标题
     */
    private String productTitle;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 团购价
     */
    private BigDecimal salePrice;

    /**
     * 正常价
     */
    private BigDecimal positivePrice;

    /**
     * 总金额
     */
    private BigDecimal totalMoney;

    /**
     * 购买数量
     */
    private Integer buyCount;

    /**
     * 团类型 1 分享团 2 自购团
     */
    private Integer groupType;

    /**
     * 团剩余时间 精确ms
     */
    private Long groupSurplusTime;

    /**
     * 剩余多少人成团
     */
    private Integer surplusCount;

    /**
     * 成团人数上限
     */
    private Integer upperLimit;

    /**
     * 成团人数下限
     */
    private Integer lowerLimit;

    /**
     * 参团人数
     */
    private Integer joinNum;

    /**
     * 团状态
     */
    private Integer groupStatus;

    /**
     * 当前用户是否加入
     */
    private Integer currWithJoin;

    /**
     * 已经参团好友数量
     */
    private Integer friendNum;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 当前用户角色
     */
    private Integer currUserRole;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String userImage;

    /**
     * 订单号
     */
    private String orderNo;


    /**
     * 是否特卖 0 否 1 是
     */
    private Integer isSupSale;

    /**
     * 是否是多规格 0 否 1 是
     */
    private Integer isMulNormal;

    /**
     * 当前用户是否是vip是否登陆过app 0 没有登陆过 1 登陆过
     */
    private Integer isVipLogined;

    /**
     * 是否是虚拟商品
     */
    private Integer isExistsVirtual;

    /**
     * 0 新创建 1 已存在
     */
    private Integer shareGroupCreateType;

    /**
     * 成员是否参团成功
     */
    private Integer isJoin;

    /**
     * 是否已经退款
     */
    private Integer refundYn;

    private List<GroupUserBo> groupUserBos;

    private List<EvaluationBo> evaluationBos;

    private Long shopId;

    /**
     * 短标题
     */
    private String shortTitle;

}
