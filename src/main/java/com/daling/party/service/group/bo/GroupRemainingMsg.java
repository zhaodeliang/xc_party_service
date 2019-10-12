package com.daling.party.service.group.bo;

import com.daling.party.domain.group.enums.GroupMessageEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by lilindong on 2019/4/11.
 * 拼团即将结束
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRemainingMsg {
    /**
     * 团编码
     */
    private String group_code;

    /**
     * 团长Id
     */
    private Long head_id;

    /**
     * 消息编码code
     */
    GroupMessageEnum groupMessageEnum;
}
