package com.daling.party.feign.ao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jiwei.xue
 * @date 2019/4/23 15:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAo implements Serializable {

    private static final long serialVersionUID = 3879072843985769878L;

    /**
     * 商品id
     */
    private Long goodId;

    /**
     * sku
     */
    private String sku;

    /**
     * 品牌id
     */
    private Long brandId;
}
