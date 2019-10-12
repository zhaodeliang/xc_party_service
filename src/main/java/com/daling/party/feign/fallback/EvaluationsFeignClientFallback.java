package com.daling.party.feign.fallback;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.EvaluationsFeignClient;
import com.daling.party.feign.ao.EvaluationAo;
import com.daling.party.feign.to.EvaluationTo;

import java.util.List;

/**
 * @author jiwei.xue
 * @date 2019/4/29 12:29
 */
public class EvaluationsFeignClientFallback implements EvaluationsFeignClient {

    @Override
    public FeignResponse<List<EvaluationTo>> queryEvaList(EvaluationAo evaluationAo) {
        FeignResponse feignResponse = FeignResponse.defaultFeignResponse();
        feignResponse.setErrorMsg("通过skus获取评价信息网络异常");
        return feignResponse;
    }
}
