package com.daling.party.feign;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.fallback.CouponFeignClientFallback;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@EnableHystrix
@FeignClient(name = "coupon", url = "${coupon.host}", fallback = CouponFeignClientFallback.class)
@RequestMapping("/inner/coupon")
public interface CouponFeignClient {

    /**
     * 获取用户针对此商品是否可用券
     *
     * @param userId
     * @param product
     * @return
     */
    @RequestMapping(value = "findOptimalBatchCoupon", method = RequestMethod.POST)
    FeignResponse<Map<String, Integer>> findOptimalBatchCoupon(@RequestParam("userId") Long userId,
                                                               @RequestParam("product") String product);

}
