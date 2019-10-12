package com.daling.party.controller.group.req;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jiwei.xue
 * @date 2019/4/9 17:58
 */
@Data
@Builder
public class GroupRequest implements Serializable {

    private static final long serialVersionUID = -6899876123222186892L;

    private ProductRequest productRequest;

    private GroupInfoRequest groupInfoRequest;

    private GroupUserRequest groupUserRequest;

    /**
     * 商品信息
     */
    @Data
    public static class ProductRequest {

        /**
         * 商品封面
         */
        @NotBlank(message = "productImg is null")
        private String productImg;

        /**
         * 商品名称
         */
        @NotBlank(message = "productName is null")
        private String productName;

        /**
         * 商品sku
         */
        @NotBlank(message = "productSku is null")
        private String productSku;

        /**
         * 商品正价
         */
        @NotBlank(message = "positivePrice is null")
        private String positivePrice;

        /**
         * 团购价格
         */
        @NotBlank(message = "salePrice is null")
        private String salePrice;

        /**
         * 总价格
         */
        @NotBlank(message = "totalMoney is null")
        private String totalMoney;
    }

    /**
     * 团信息
     */
    @Data
    public static class GroupInfoRequest {

        /**
         * 活动code
         */
        private String activityCode;

        /**
         * 团结束时间
         */
        @NotBlank(message = "groupEndDate is null")
        private Date groupEndDate;

        /**
         * 团的类型
         */
        @NotBlank(message = "groupType is null")
        private Integer groupType;

    }

    /**
     * 用户信息
     */
    @Data
    public static class GroupUserRequest {

        /**
         * 用户id
         */
        @NotNull(message = "userId is null")
        private Long userId;

        /**
         * 用户头像
         */
        @NotBlank(message = "headImage is null")
        private String headImage;

        /**
         * 用户角色
         */
        @NotNull(message = "userRole is null")
        private Integer userRole;

        /**
         * 用户昵称
         */
        @NotNull(message = "nickName is null")
        private String nickName;
    }
}
