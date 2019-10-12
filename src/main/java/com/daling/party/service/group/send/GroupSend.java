package com.daling.party.service.group.send;

import com.daling.party.service.group.bo.GroupRemainingMsg;
import com.daling.party.service.group.bo.ValidBo;

import java.util.List;
import java.util.Map;

/**
 * @author jiwei.xue
 * @date 2019/4/11 10:55
 */
public interface GroupSend {

    /**
     * 参团失败 发起退款
     *
     * @param orderNo
     */
    void refundSend(String orderNo);

    /**
     * 成团成功 给团内所有成员发起通知
     *
     * @param GroupCode
     */
    void groupSuccessSend(String GroupCode, List<Long> userIds);

    /**
     * 参团成功 给团长发起通知
     *
     * @param headId
     * @param groupCode
     * @param nickName
     * @param productName
     */
    void joinSuccessSend(Long headId, String groupCode, String nickName, String productName);

    /**
     * 2个小时提醒
     * @param groupRemainingMsg
     */
    void sendGroupEndingMsgToMq(GroupRemainingMsg groupRemainingMsg);

    /**
     * 发货退款信息 成团和插入
     * @param validBo
     */
    void sendValidGroupMsgToMq(ValidBo validBo);

    /**
     * 拼团失败提醒
     * @param groupCode
     * @param members
     */
    void sendGroupShoppingFailMessage(String groupCode, List members);
}
