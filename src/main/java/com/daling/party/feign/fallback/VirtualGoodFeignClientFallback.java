package com.daling.party.feign.fallback;

import com.daling.party.domain.vo.FeignResult;
import com.daling.party.feign.VirtualGoodFeignClient;
import com.daling.party.feign.to.VirtualGoodTo;

/**
 * @author jiwei.xue
 * @date 2019/4/29 14:38
 */
public class VirtualGoodFeignClientFallback implements VirtualGoodFeignClient {

    @Override
    public FeignResult<VirtualGoodTo> queryVirtualGood(String sku) {
        FeignResult feignResult = FeignResult.defaultFeignResult();
        feignResult.setMessage("通过sku查询虚拟商品网络异常");
        return feignResult;
    }
}
