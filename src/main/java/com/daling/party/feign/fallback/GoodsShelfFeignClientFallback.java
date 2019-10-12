package com.daling.party.feign.fallback;

import com.daling.party.domain.po.TGoodShelf;
import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.GoodsShelfFeignClient;

import java.util.List;

/**
 * @author jiwei.xue
 * @date 2019/4/29 14:37
 */
public class GoodsShelfFeignClientFallback implements GoodsShelfFeignClient {

    @Override
    public FeignResponse<List<TGoodShelf>> findBySku(String skus) {
        FeignResponse feignResponse = FeignResponse.defaultFeignResponse();
        feignResponse.setErrorMsg("通过skus查询商品上下架信息网络异常");
        return feignResponse;
    }
}
