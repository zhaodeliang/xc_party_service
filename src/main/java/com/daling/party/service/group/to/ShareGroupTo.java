package com.daling.party.service.group.to;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jiwei.xue
 * @date 2019/4/28 16:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareGroupTo implements Serializable {

    private static final long serialVersionUID = 2176734490988228291L;

    /**
     * 团code
     */
    private String groupCode;

    /**
     * 0 新创建的 1 已存在的
     */
    private Integer type;
}
