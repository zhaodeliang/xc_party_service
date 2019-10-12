package com.daling.party.feign;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.domain.vo.UserVO;
import com.daling.party.feign.fallback.UCenterFeignClientFallback;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@EnableHystrix
@FeignClient(name = "xcucenter", url = "${xcucenter.host}", fallback = UCenterFeignClientFallback.class)
@RequestMapping("/xc_uc")
public interface UCenterFeignClient {

    @RequestMapping(value = "/inner/uc/auth.do", method = RequestMethod.GET)
    FeignResponse<UserVO> getUser(@RequestHeader("utoken") String uToken,
                                  @RequestHeader("uid") String uid,
                                  @RequestHeader("platform") String platform,
                                  @RequestHeader("clientid") String clientId);

    @RequestMapping(value = "/inner/uc/queryByMobile.do", method = RequestMethod.GET)
    FeignResponse<UserVO> getUserByMobile(@RequestParam("mobile") String mobile);

    @RequestMapping(value = "/inner/uc/queryById.do", method = RequestMethod.GET)
    FeignResponse<UserVO> getUserById(@RequestParam("uid") Long uid);
}
