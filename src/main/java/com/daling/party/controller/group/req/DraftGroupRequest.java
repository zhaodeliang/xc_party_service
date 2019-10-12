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
 * @date 2019/4/9 21:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DraftGroupRequest implements Serializable {

    private static final long serialVersionUID = 1206374480445924095L;

    /**
     * 团长id 用户id
     */
    @NotNull(message = "headId is null")
    private Long headId;

    /**
     * 活动code
     */
    @NotBlank(message = "activityCode is null")
    private String activityCode;

    /**
     * spu
     */
    @NotBlank(message = "spu is null")
    private String spu;
}
