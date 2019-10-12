package com.daling.party.service.group.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lilindong on 2019/4/8.
 * 拼团详情
 */
@Data
public class UserGroupBo implements Serializable {
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
    private Date createTime; // 团创建时间
    private String productSku; // 拼团商品Sku
    private String productName; // 拼团商品名称
    private String productImg; // 拼团商品图片
    private BigDecimal productPositivePerice; // 拼团商品商品正价
    private BigDecimal productSalePerice; // 拼团商品销售价
    private BigDecimal productTotalMoney; // 拼团商品总价
    private Integer orderStatus; // 订单状态 1、下单 2、退货
    private Integer joinType; // 加入方式：1、分享 2、自购
    private Integer groupRole; // 角色：1、团长 2、团员 3、插队
    private Date joinTime; // 加入时间

}

