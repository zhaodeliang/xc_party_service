package com.daling.party.infrastructure.transport.client;

import com.daling.party.infrastructure.transport.hystrix.DemoHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * demo 此处与第三方接口交互
 *
 */
@FeignClient(name="wechat",url = "https://api.mch.weixin.qq.com",fallback = DemoHystrix.class)
public interface DemoFeginClient {


}
