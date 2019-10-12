package com.daling.party.feign.fallback;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.CouponFeignClient;

import java.util.Map;

public class CouponFeignClientFallback implements CouponFeignClient {

    @Override
    public FeignResponse<Map<String, Integer>> findOptimalBatchCoupon(Long userId, String product) {
        FeignResponse feignResponse = FeignResponse.defaultFeignResponse();
        feignResponse.setErrorMsg("获取券信息标签网络异常");
        return feignResponse;
    }
}
