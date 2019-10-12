package com.daling.party.feign;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.ao.PriceAo;
import com.daling.party.feign.fallback.PriceFeignClientFallback;
import com.daling.party.feign.to.PriceTo;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jiwei.xue
 * @date 2019/5/5 17:36
 */
@EnableHystrix
@FeignClient(name = "price", url = "${price.host}", fallback = PriceFeignClientFallback.class)
@RequestMapping("/price/cur")
public interface PriceFeignClient {

    @RequestMapping(value = "price/cur", method = RequestMethod.POST, consumes = "application/json")
    FeignResponse<PriceTo> qureyPriceCur(@RequestBody PriceAo priceAo);


}
