package com.daling.party.feign;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.feign.ao.EvaluationAo;
import com.daling.party.feign.fallback.EvaluationsFeignClientFallback;
import com.daling.party.feign.fallback.UCenterFeignClientFallback;
import com.daling.party.feign.to.EvaluationTo;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@EnableHystrix
@FeignClient(name = "dalcec", url = "${dalcec.host}", fallback = EvaluationsFeignClientFallback.class)
@RequestMapping("/dal_cec/inner/cds")
public interface EvaluationsFeignClient {

    @RequestMapping(value = "evaluation/groupbuy/list.do", method = RequestMethod.POST, consumes = "application/json")
    FeignResponse<List<EvaluationTo>> queryEvaList(@RequestBody EvaluationAo evaluationAo);
}