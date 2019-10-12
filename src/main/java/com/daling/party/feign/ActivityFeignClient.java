package com.daling.party.feign;

import com.daling.party.domain.vo.FeignResult;
import com.daling.party.feign.fallback.ActivityFeignClientFallback;
import com.daling.party.feign.to.ActivityTo;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@EnableHystrix
@FeignClient(name = "activity", url = "${activity.host}", fallback = ActivityFeignClientFallback.class)
@RequestMapping("/activity/cur")
public interface ActivityFeignClient {

    /**
     * 获取活动信息 没有商品信息
     *
     * @param actCode
     * @return
     */
    @RequestMapping(value = "activity/getActivityByActCode", method = RequestMethod.POST)
    FeignResult<ActivityTo> queryActivityByActCode(@RequestParam("actCode") String actCode);

    /**
     * 获取活动信息 有商品信息
     *
     * @param sku
     * @return
     */
    @RequestMapping(value = "activity/getActivity", method = RequestMethod.POST)
    FeignResult<ActivityTo> queryActivityBySku(@RequestParam("sku") String sku);
}
