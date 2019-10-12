package com.daling.party.service.group.converter;

import com.daling.party.service.group.bo.GroupBo;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author jiwei.xue
 * @date 2019/4/9 15:06
 */
public class GroupConverter {

    public static GroupBo map2Bo(Map<String, Object> groupMap) {
        if (CollectionUtils.isEmpty(groupMap)) {
            return null;
        }
        return GroupBo.builder()
                .buyCount((Integer) groupMap.get("buyCount"))
                .groupCode(String.valueOf(groupMap.get("groupCode")))
                .groupEndDate(new Date((Long) (groupMap.get("groupEndDate"))))
                .groupStartDate(new Date((Long) (groupMap.get("groupStartDate"))))
                .orderNo(String.valueOf(groupMap.get("orderNo")))
                .orderStatus((Integer) groupMap.get("orderStatus"))
                .sku(String.valueOf(groupMap.get("sku")))
                .totalMoney(BigDecimal.valueOf((Double) groupMap.get("totalMoney")))
                .userId(Long.valueOf((String.valueOf(groupMap.get("userId")))))
                .userRole((Integer) groupMap.get("userRole"))
                .refundYn((Integer) groupMap.get("refundYn"))
                .build();
    }
}
