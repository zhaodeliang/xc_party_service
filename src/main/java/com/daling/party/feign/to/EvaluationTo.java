package com.daling.party.feign.to;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiwei.xue
 * @date 2019/4/11 14:30
 */
@Data
public class EvaluationTo implements Serializable {

    private static final long serialVersionUID = 3989840713548742737L;

    /**
     * 内容
     */
    private String content;

    /**
     * sku
     */
    private String sku;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户头像
     */
    private String userLogo;
}
