package com.daling.party.feign;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.fallback.GoodDetailFeignClientFallback;
import com.daling.party.feign.to.GoodDetailTo;
import com.daling.party.feign.to.GoodGroupDetailTo;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@EnableHystrix
@FeignClient(name = "xcsale", url = "${xcsale.host}", fallback = GoodDetailFeignClientFallback.class)
@RequestMapping("/xc_sale/openapi/goods")
public interface GoodDetailFeignClient {

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    FeignResponse<GoodDetailTo> queryGoodDetail(@RequestHeader("utoken") String uToken,
                                                @RequestHeader("uid") String uid,
                                                @RequestHeader("platform") String platform,
                                                @RequestHeader("clientid") String clientId,
                                                @RequestParam("trackId") String trackId,
                                                @RequestParam("id") Long id,
                                                @RequestParam("sku") String sku,
                                                @RequestParam("viewType") Integer viewType);

    @RequestMapping(value = "group/detail.do", method = RequestMethod.GET)
    FeignResponse<GoodGroupDetailTo> queryGoodGroupDetail(@RequestParam("sku") String sku);

}
