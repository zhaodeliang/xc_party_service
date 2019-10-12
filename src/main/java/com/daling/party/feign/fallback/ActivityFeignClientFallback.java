package com.daling.party.feign.fallback;

import com.daling.party.domain.vo.FeignResult;
import com.daling.party.feign.ActivityFeignClient;
import com.daling.party.feign.to.ActivityTo;

/**
 * @author jiwei.xue
 * @date 2019/4/29 12:24
 */
public class ActivityFeignClientFallback implements ActivityFeignClient {

    @Override
    public FeignResult<ActivityTo> queryActivityByActCode(String actCode) {
        FeignResult feignResult = FeignResult.defaultFeignResult();
        feignResult.setMessage("通过活动code查询拼团活动网络异常");
        return feignResult;
    }


    @Override
    public FeignResult<ActivityTo> queryActivityBySku(String sku) {
        FeignResult feignResult = FeignResult.defaultFeignResult();
        feignResult.setMessage("通过sku查询拼团活动网络异常");
        return feignResult;
    }
}
