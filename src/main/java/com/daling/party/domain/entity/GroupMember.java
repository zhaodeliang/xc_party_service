package com.daling.party.domain.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
@Data
@Table(name = "s_group_member")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //团成员用户ID
    private Long userId;
    //头像信息
    private String userImg;
    //昵称信息
    private String username;
    //团ID
    private Long groupId;
    //sku
    private String sku;
    //商品标题
    private String productName;
    //图片地址
    private String productImg;
    //正常价格
    private BigDecimal positivePrice;
    //销售价格
    private BigDecimal salePrice;
    // 总价
    private BigDecimal totalMoney;
    //订单状态  1、下单 2、退货
    private Integer orderStatus;
    //团类型 1：分享 2：自购
    private Integer joinType;
    //团角色 1、团长 2、团员 3、插队
    private Integer groupRole;
    //订单号，分享团团长可为null
    private String orderNo;
    // 购买数量
    private Integer buyNum;
    // 是否新用户
    private Integer isNew;
    // 创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    // 是否新用户
    private Integer validFlag;
    // 创建时间
    private Integer remindMesFlag;
    // 是否参团成功
    private Integer isJoin;
}
