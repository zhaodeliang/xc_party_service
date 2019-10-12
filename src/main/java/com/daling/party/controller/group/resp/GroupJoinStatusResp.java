package com.daling.party.controller.group.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by lilindong on 2019/4/17.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupJoinStatusResp {

    private int joinStatus; // 0：团不存在;1：团未开始;2：正在拼团;3：已成团，但未达到人数上限，且未达到拼团结束时间;4：已成团，但未达到人数上限，且拼团已经结束;5：已成团，且已到达人数上限，但未达到拼团结束时间;6：已成团，且已到达人数上限，且拼团已经结束;7：拼团失败
    private int isJoin; // 用户是否加入团
}
