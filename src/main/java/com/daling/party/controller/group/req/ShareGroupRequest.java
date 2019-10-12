package com.daling.party.controller.group.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author jiwei.xue
 * @date 2019/4/13 11:10
 */
@Data
public class ShareGroupRequest implements Serializable {

    private static final long serialVersionUID = 1572446118150870930L;

    /**
     * sku
     */
    @NotBlank(message = "sku为空！")
    private String sku;

    /**
     * spu
     */
    @NotBlank(message = "spu为空！")
    private String spu;

    /**
     * 团开始时间
     */
    @NotNull(message = "团开始时间为空！")
    private String groupStartDate;

    /**
     * 团结束时间
     */
    @NotNull(message = "团结束时间为空！")
    private String groupEndDate;

    /**
     * 用户id
     */
    @NotNull(message = "用户id为空！")
    private String userId;

    /**
     * 用户角色 是否是新用户
     */
    @NotNull(message = "用户角色标签为空！")
    private String userRole;


}
