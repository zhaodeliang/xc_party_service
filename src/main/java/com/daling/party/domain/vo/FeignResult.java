package com.daling.party.domain.vo;

import com.daling.party.infrastructure.utils.XcHeadWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiwei.xue
 * @date 2019/4/12 20:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeignResult<T> {

    private Integer code;

    private String message;

    private T data;

    public boolean isSuccess() {
        return 200 == code;
    }

    public boolean isFailure() {
        return 200 != code;
    }

    /**
     * 断路器返回默认的result
     *
     * @return
     */
    public static FeignResult defaultFeignResult() {
        return FeignResult.builder()
                .code(XcHeadWrapper.StatusEnum.Failed.getCode())
                .build();
    }
}
