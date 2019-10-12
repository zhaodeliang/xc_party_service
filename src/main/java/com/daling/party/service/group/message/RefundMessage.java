package com.daling.party.service.group.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jiwei.xue
 * @date 2019/4/17 18:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundMessage implements Serializable {

    private static final long serialVersionUID = 8772019641833509047L;
    /**
     * 订单
     */
    private String orderNo;

    /**
     * 类型
     */
    private Integer type;

    /**
     * ip
     */
    private String ip;

    /**
     * 时间
     */
    private Long opTime;

}
