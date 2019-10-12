package com.daling.party.controller.group.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author jiwei.xue
 * @date 2019/4/8 21:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDetailResp implements Serializable {

    /**
     * 订单状态
     */
    private String orderNo;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 当前用户角色
     */
    private Integer currUserRole;

    /**
     * 已经参团的好友数量
     */
    private Integer friendNum;

    /**
     * 是否VIP并且是否登录过
     */
    private Integer isVipLogined;

    /**
     * 是否是虚拟商品
     */
    private Integer isExistsVirtual;

    /**
     * 商品信息
     */
    private Product product;

    /**
     * 评价信息
     */
    private List<Evaluation> evaluations;

    /**
     * 用户信息
     */
    private List<User> users;

    /**
     * 团信息
     */
    private Group group;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

        /**
         * sku
         */
        private String sku;

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
        private String salePrice;

        /**
         * 正常价
         */
        private String positivePrice;

        /**
         * 成团人数
         */
        private Integer lowerLimit;

        /**
         * 是否是多规格的
         */
        private Integer isMulNormal;

        /**
         * 标签信息
         */
        private List<String> tags;

        /**
         * 短标题
         */
        private String shortTitle;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Evaluation {

        /**
         * 评价用户头像
         */
        private String image;

        /**
         * 评价内容
         */
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {

        /**
         * 用户id
         */
        private Long userId;

        /**
         * 参团人员头像
         */
        private String image;

        /**
         * 是否新
         */
        private Integer isNew;

        /**
         * 昵称
         */
        private String nickName;

        /**
         * 用户角色 1 团长 2 成员 3 插入者
         */
        private Integer userRole;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Group {

        /**
         * 团code
         */
        private String groupCode;

        /**
         * 团类型 1 分享团 2 自购团
         */
        private Integer groupType;

        /**
         * 团状态  0 草稿 1 成功 2 失败 3 进行中
         */
        private Integer groupStatus;

        /**
         * 团剩余时间 精确ms
         */
        private Long groupSurplusTime;

        /**
         * 团结束时间
         */
        private String groupEndDate;

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
         * 当前用户是否加入
         */
        private Integer currWithJoin;
    }

}
