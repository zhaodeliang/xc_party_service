package com.daling.party.service.group.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daling.party.domain.group.enums.GroupMessageEnum;
import com.daling.party.infrastructure.base.AmpqMsg;
import com.daling.party.infrastructure.config.AmqpDeclareConfig;
import com.daling.party.infrastructure.enums.MessageEnum;
import com.daling.party.infrastructure.utils.FreeIPUtils;
import com.daling.party.infrastructure.utils.IPAddressUtil;
import com.daling.party.service.group.bo.GroupRemainingMsg;
import com.daling.party.service.group.bo.ValidBo;
import com.daling.party.service.group.message.RefundMessage;
import com.daling.party.service.group.send.GroupSend;
import com.daling.ucclient.tools.Jackson2Helper;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.testng.collections.Maps;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author jiwei.xue
 * @date 2019/4/11 10:56
 */
@Service
@Slf4j
public class GroupSendImpl implements GroupSend {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void refundSend(String orderNo) {
        RefundMessage refundMessage = buildRefundMessage(orderNo);
        try {
            String message = JSONObject.toJSONString(refundMessage);
            log.info("user refund, message = {}", message);
            rabbitTemplate.convertAndSend(AmqpDeclareConfig.GROUP_MEMBER_VALID_EXCHANGE, null, message);
        } catch (AmqpException e) {
            log.error("user refund is fail", e);
        }
    }

    @Override
    public void groupSuccessSend(String groupCode, List<Long> userIds) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("group_code", groupCode);
        data.put("user_id_array", userIds);
        try {
            String message = JSONObject.toJSONString(buildAmpqMsg(GroupMessageEnum.GROUP_SUCCESS.getCode(), data));
            log.info("groupSuccessSend, message = {}", message);
            rabbitTemplate.convertAndSend(AmqpDeclareConfig.GROUP_SHOPPING_MESSAGE_EXCHANGE, null, message);
        } catch (AmqpException e) {
            log.error("groupSuccessSend is fail", e);
        }
    }

    @Override
    public void joinSuccessSend(Long headId, String groupCode, String nickName, String productName) {
        Map<String, Object> data = Maps.newHashMap();
        data.put("group_code", groupCode);
        data.put("head_id", headId);
        data.put("friend_nick_name", nickName);
        data.put("product_name", productName);
        try {
            String message = JSONObject.toJSONString(buildAmpqMsg(GroupMessageEnum.JOIN_SUCCESS.getCode(), data));
            log.info("joinSuccessSend, message = {}", message);
            rabbitTemplate.convertAndSend(AmqpDeclareConfig.GROUP_SHOPPING_MESSAGE_EXCHANGE, null, message);
        } catch (AmqpException e) {
            log.error("joinSuccessSend is fail", e);
        }
    }

    @Override
    public void sendGroupEndingMsgToMq(GroupRemainingMsg groupRemainingMsg) {
        String text = JSON.toJSONString(buildAmpqMsg(groupRemainingMsg.getGroupMessageEnum().getCode(), groupRemainingMsg));
        log.info("sendGroupEndingMsgToMq message={}",text);
        rabbitTemplate.convertAndSend(AmqpDeclareConfig.GROUP_SHOPPING_MESSAGE_EXCHANGE, null,text.getBytes(Charsets.UTF_8));
    }


    @Override
    public void sendValidGroupMsgToMq(ValidBo validBo) {
        String text = Jackson2Helper.toJsonString(validBo);
        log.info("sendValidGroupMsgToMq message ={}",text);
        rabbitTemplate.convertAndSend(AmqpDeclareConfig.GROUP_MEMBER_VALID_EXCHANGE, null,text.getBytes(Charsets.UTF_8));
    }

    @Override
    public void sendGroupShoppingFailMessage(String groupCode,List members) {
        Map<String, Object> failureMap = new HashMap<>(3);
        failureMap.put("group_code", groupCode);
        failureMap.put("user_id_array", members);
        String text = Jackson2Helper.toJsonString(buildAmpqMsg(GroupMessageEnum.GROUP_SHOPPING_FAIL_CODE.getCode(),failureMap));

        log.info("sendGroupShoppingFailMessage message ={}",text);
        rabbitTemplate.convertAndSend(AmqpDeclareConfig.GROUP_SHOPPING_MESSAGE_EXCHANGE, null,text.getBytes(Charsets.UTF_8));
    }

    /**
     * 通过格式
     *
     * @param code
     * @param body
     * @return
     */
    private AmpqMsg buildAmpqMsg(String code, Object body) {
        return AmpqMsg.builder()
                .track_id(UUID.randomUUID().toString())
                .event_time(System.currentTimeMillis())
                .code(code)
                .body(body)
                .client_ip(FreeIPUtils.getIP())
                .build();
    }

    /**
     * 构建退款信息
     *
     * @param orderNo
     * @return
     */
    private RefundMessage buildRefundMessage(String orderNo) {
        return RefundMessage.builder()
                .orderNo(orderNo)
                .type(MessageEnum.refund.getCode())
                .opTime(System.currentTimeMillis())
                .ip(IPAddressUtil.getLocalIPAddress())
                .build();
    }
}
