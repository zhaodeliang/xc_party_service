package com.daling.party.service.group.bo;

import com.daling.party.infrastructure.utils.FreeIPUtils;
import lombok.Data;

import java.util.UUID;

/**
 * @author dl.zhao
 * @date 2019/4/10 12:10
 */
@Data
public class ValidBo {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 操作类型
     */
    private Integer type;

    protected String track_id = UUID.randomUUID().toString();
    protected Long event_time = System.currentTimeMillis();
    protected String client_ip = FreeIPUtils.getIP();

}
