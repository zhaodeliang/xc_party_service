package com.daling.party.feign.fallback;

import com.daling.party.domain.vo.FeignResponse;
import com.daling.party.domain.vo.UserVO;
import com.daling.party.feign.UCenterFeignClient;

/**
 * @author jiwei.xue
 * @date 2019/4/12 12:31
 */
public class UCenterFeignClientFallback implements UCenterFeignClient {

    @Override
    public FeignResponse<UserVO> getUser(String uToken, String uid, String platform, String clientId) {
        FeignResponse feignResponse = FeignResponse.defaultFeignResponse();
        feignResponse.setErrorMsg("获取用户信息网络异常");
        return feignResponse;
    }

    @Override
    public FeignResponse<UserVO> getUserByMobile(String mobile) {
        FeignResponse feignResponse = FeignResponse.defaultFeignResponse();
        feignResponse.setErrorMsg("通过手机号获取用户信息网络异常");
        return feignResponse;
    }

    @Override
    public FeignResponse<UserVO> getUserById(Long uid) {
        FeignResponse feignResponse = FeignResponse.defaultFeignResponse();
        feignResponse.setErrorMsg("通过uid获取用户信息网络异常");
        return feignResponse;
    }
}
