package com.daling.party.feign.fallback;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.GoodDetailFeignClient;
import com.daling.party.feign.to.GoodDetailTo;
import com.daling.party.feign.to.GoodGroupDetailTo;

/**
 * @author jiwei.xue
 * @date 2019/4/29 12:30
 */
public class GoodDetailFeignClientFallback implements GoodDetailFeignClient {

    @Override
    public FeignResponse<GoodDetailTo> queryGoodDetail(String uToken, String uid, String platform, String clientId, String trackId, Long id, String sku, Integer viewType) {
        FeignResponse feignResponse = FeignResponse.defaultFeignResponse();
        feignResponse.setErrorMsg("获取商品详情网络异常");
        return feignResponse;
    }

    @Override
    public FeignResponse<GoodGroupDetailTo> queryGoodGroupDetail(String sku) {
        FeignResponse feignResponse = FeignResponse.defaultFeignResponse();
        feignResponse.setErrorMsg("获取拼团商品详情网络异常");
        return feignResponse;
    }
}
