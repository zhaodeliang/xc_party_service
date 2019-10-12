package com.daling.party.feign;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.fallback.GoodInfoFeignClientFallback;
import com.daling.party.feign.fallback.UCenterFeignClientFallback;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@EnableHystrix
@FeignClient(name = "goodcenter", url = "${goodcenter.host}", fallback = GoodInfoFeignClientFallback.class)
@RequestMapping("/api/goods")
public interface GoodInfoFeignClient {

    @RequestMapping(value = "sku/raw/info/{sku}", method = RequestMethod.GET)
    FeignResponse<Map<String, Object>> queryGoodInfo(@RequestHeader("source") String source,
                                                     @PathVariable("sku") String sku);
}
