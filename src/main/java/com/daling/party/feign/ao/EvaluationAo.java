package com.daling.party.feign.ao;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiwei.xue
 * @date 2019/4/15 15:16
 */
@Data
public class EvaluationAo implements Serializable {

    private static final long serialVersionUID = 5607164562744955684L;

    private String sku;
}
