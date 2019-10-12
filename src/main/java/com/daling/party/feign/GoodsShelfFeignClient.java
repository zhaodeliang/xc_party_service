package com.daling.party.feign;

import com.daling.party.domain.po.TGoodShelf;
import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.fallback.GoodsShelfFeignClientFallback;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@EnableHystrix
@FeignClient(name = "xcsale", url = "${xcsale.host}", fallback = GoodsShelfFeignClientFallback.class)
@RequestMapping("/xc_sale/goodself")
public interface GoodsShelfFeignClient {

    @RequestMapping(value = "findBySku.do", method = RequestMethod.POST)
    FeignResponse<List<TGoodShelf>> findBySku(@RequestParam("skus") String skus);
}
