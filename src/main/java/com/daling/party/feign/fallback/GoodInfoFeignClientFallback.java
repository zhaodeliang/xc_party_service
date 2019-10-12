package com.daling.party.feign.fallback;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.GoodInfoFeignClient;

import java.util.Map;

/**
 * @author jiwei.xue
 * @date 2019/4/29 14:36
 */
public class GoodInfoFeignClientFallback implements GoodInfoFeignClient {

    @Override
    public FeignResponse<Map<String, Object>> queryGoodInfo(String source, String sku) {
        FeignResponse feignResponse = FeignResponse.defaultFeignResponse();
        feignResponse.setErrorMsg("获取商品信息网络异常");
        return feignResponse;
    }
}
