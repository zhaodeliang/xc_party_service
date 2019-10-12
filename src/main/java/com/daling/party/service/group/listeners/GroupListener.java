package com.daling.party.service.group.listeners;

import com.daling.common.dmonitor.DMonitor;
import com.daling.party.infrastructure.config.AmqpDeclareConfig;
import com.daling.party.service.group.GroupService;
import com.daling.party.service.group.bo.GroupBo;
import com.daling.party.service.group.converter.GroupConverter;
import com.daling.ucclient.tools.Jackson2Helper;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author jiwei.xue
 * @date 2019/4/9 12:36
 */
@Component
@Slf4j
public class GroupListener {

    @Resource
    private GroupService groupService;

    /**
     * 创建团事件
     * @param message
     */
    @RabbitListener(queues = AmqpDeclareConfig.GROUP_PAY_SUCCESS_EVENT_QUEUE, concurrency = "15")
    public void createGroup(Message message) {
        String body;
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            body = new String(message.getBody(), "utf-8");
            log.info("join group, message = {}", body);
            Map<String, Object> groupMap = Jackson2Helper.parsingObject(body, Map.class);
            GroupBo groupBo = GroupConverter.map2Bo(groupMap);
            //测试用户睡2秒
//            if (groupBo.getUserId().equals(1106L) || groupBo.getUserId().equals(8889372L)) {
//                log.info("用户睡眠两秒钟");
//                Thread.sleep(7000L);
//            }
            groupService.joinGroup(groupBo);
        } catch (Exception e) {
            log.error("join group fail:", e);
        } finally {
            DMonitor.recordOne("apiMq_join_group_message", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }
}
