package com.daling.party.controller.group.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author jiwei.xue
 * @date 2019/4/28 17:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinGroupRequest implements Serializable {

    private static final long serialVersionUID = -8920407538110853983L;

    @NotBlank(message = "totalMoney is null")
    private String totalMoney;

    @NotNull(message = "buyCount is null")
    private Integer buyCount;

    @NotBlank(message = "sku is null")
    private String sku;

    @NotBlank(message = "groupCode is null")
    private String groupCode;

    @NotBlank(message = "groupStartDate is null")
    private String groupStartDate;

    @NotBlank(message = "groupEndDate is null")
    private String groupEndDate;

    @NotNull(message = "userRole is null")
    private Integer userRole;

    @NotNull(message = "userId is null")
    private Long userId;

    @NotBlank(message = "orderNo is null")
    private String orderNo;

    @NotNull(message = "orderStatus is null")
    private Integer orderStatus;
}
