package com.daling.party.feign.to;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiwei.xue
 * @date 2019/4/12 14:28
 */
@Data
public class GoodsLabelTo implements Serializable {

    private static final long serialVersionUID = -817317292409341078L;

    private Long goodsId;

    private Long labelId;

    private String labelLogo;

    private Integer height;

    private Integer width;
}
