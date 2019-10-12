package com.daling.party.feign.fallback;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.PriceFeignClient;
import com.daling.party.feign.ao.PriceAo;
import com.daling.party.feign.to.PriceTo;

/**
 * @author jiwei.xue
 * @date 2019/5/5 17:48
 */
public class PriceFeignClientFallback implements PriceFeignClient {

    @Override
    public FeignResponse<PriceTo> qureyPriceCur(PriceAo priceAo) {
        FeignResponse feignResponse = FeignResponse.defaultFeignResponse();
        feignResponse.setErrorMsg("获取一般价格网络异常");
        return feignResponse;
    }
}
