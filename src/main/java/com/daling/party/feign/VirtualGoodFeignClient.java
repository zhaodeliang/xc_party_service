package com.daling.party.feign;

import com.daling.party.domain.vo.FeignResult;
import com.daling.party.feign.fallback.UCenterFeignClientFallback;
import com.daling.party.feign.fallback.VirtualGoodFeignClientFallback;
import com.daling.party.feign.to.VirtualGoodTo;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jiwei.xue
 * @date 2019/4/24 20:16
 */
@EnableHystrix
@FeignClient(name = "xcsale", url = "${xcsale.host}", fallback = VirtualGoodFeignClientFallback.class)
@RequestMapping("/xc_sale/inner/api/good")
public interface VirtualGoodFeignClient {

    /**
     * 通过sku查询虚拟商品
     *
     * @param sku
     * @return
     */
    @RequestMapping(value = "virtual.do", method = RequestMethod.GET)
    FeignResult<VirtualGoodTo> queryVirtualGood(@RequestParam("sku") String sku);
}
