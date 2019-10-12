package com.daling.party.controller.group.resp;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lilindong on 2019/4/8.
 * 拼团详情
 */
@Data
@Builder
public class UserGroupListResp implements Serializable {
    private String groupCode; // 团编码
    private Integer status; // 拼团状态 1：成功 2：失败 3：进行中
    private String groupRule; // 团规则 例：2人团
    private Long surplusTime; // 拼团剩余时间（单位秒）
    private String productSku; // 拼团商品Sku
    private String productName; // 拼团商品名称
    private String productImg; // 拼团商品图片
    private BigDecimal productPositivePerice; // 拼团商品商品正价
    private BigDecimal productSalePerice; // 拼团商品销售价
    private Date joinTime; // 加入时间

}

