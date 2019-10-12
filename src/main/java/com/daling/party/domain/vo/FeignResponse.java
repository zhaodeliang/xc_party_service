package com.daling.party.domain.vo;

import com.daling.party.infrastructure.utils.XcHeadWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeignResponse<T> {
    private String version;
    private String timestamp;
    private int status;
    private String errorMsg;
    private int elapsed;
    private T data;

    public boolean isSuccess() {
        return 0 == status;
    }

    public boolean isFailure() {
        return 0 != status;
    }

    /**
     * 断路器返回默认的result
     *
     * @return
     */
    public static FeignResponse defaultFeignResponse() {
        return FeignResponse.builder()
                .status(XcHeadWrapper.StatusEnum.Failed.getCode())
                .build();
    }
}
