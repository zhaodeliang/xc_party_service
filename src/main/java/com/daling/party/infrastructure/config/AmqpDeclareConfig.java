package com.daling.party.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class AmqpDeclareConfig {

    public static final String GROUP_SHOPPING_MESSAGE_EXCHANGE = "group_shopping_message_exchange";

    public static final String GROUP_MEMBER_VALID_EXCHANGE = "group_member_valid_exchange";

    public static final String PUBLIC_MESSAGE_EXCHANGE = "public_message_exchange";


    /**
     *  支付成功 exchange queue
     */
    public static final String GROUP_PAY_SUCCESS_EVENT_EXCHANGE = "group_pay_success_event_exchange";
    public static final String GROUP_PAY_SUCCESS_EVENT_QUEUE = "group_pay_success_event_queue";

    /**
     * 新店主拥有vip exchange queue
     */
    public static final String USER_TAGS_EVENT_EXCHANGE = "user_tags_event_exchange";
    public static final String USER_ACCOUNT_QUEUE = "user_account_queue";

    /**
     * 新店主订单处理 exchange queue
     */
    public static final String NEW_SHOP_TASK_ORDER_EXCHANGE = "new_shop_task_order_exchange";
    public static final String NEW_SHOP_TASK_ORDER_QUEUE = "user_account_queue";

    @Bean(NEW_SHOP_TASK_ORDER_QUEUE)
    public Queue newShopTaskOrderQueue() {
        return new Queue(NEW_SHOP_TASK_ORDER_QUEUE);
    }

    @Bean
    public FanoutExchange newShopTaskOrderExchange() {
        return new FanoutExchange(NEW_SHOP_TASK_ORDER_EXCHANGE, true, false);
    }

    @Bean
    public Binding newShopTaskOrderBinding() {
        return BindingBuilder.bind(newShopTaskOrderQueue()).to(newShopTaskOrderExchange());
    }

    @Bean(USER_ACCOUNT_QUEUE)
    public Queue userAccountQueue() {
        return new Queue(USER_ACCOUNT_QUEUE);
    }

    @Bean
    public FanoutExchange userTagsEventExchange() {
        return new FanoutExchange(USER_TAGS_EVENT_EXCHANGE, true, false);
    }

    @Bean
    public Binding userAccountBinding() {
        return BindingBuilder.bind(userAccountQueue()).to(userTagsEventExchange());
    }

    @Bean(GROUP_PAY_SUCCESS_EVENT_QUEUE)
    public Queue groupPaySuccessEventQueue() {
        return new Queue(GROUP_PAY_SUCCESS_EVENT_QUEUE);
    }

    @Bean
    public FanoutExchange groupPaySuccessEventExchange() {
        return new FanoutExchange(GROUP_PAY_SUCCESS_EVENT_EXCHANGE, true, false);
    }

    @Bean
    public Binding groupPaySuccessEventBinding() {
        return BindingBuilder.bind(groupPaySuccessEventQueue()).to(groupPaySuccessEventExchange());
    }

    @Bean
    public FanoutExchange refreshCouponLogExchange() {
        return new FanoutExchange(GROUP_SHOPPING_MESSAGE_EXCHANGE, true, false);
    }

    @Bean
    public FanoutExchange groupMemberValidExchange() {
        return new FanoutExchange(GROUP_MEMBER_VALID_EXCHANGE, true, false);
    }

    @Bean
    public FanoutExchange groupShoppingMessageExchange() {
        return new FanoutExchange(GROUP_SHOPPING_MESSAGE_EXCHANGE, true, false);
    }

    @Bean
    public FanoutExchange publicMessageExchange() {
        return new FanoutExchange(PUBLIC_MESSAGE_EXCHANGE, true, false);
    }
    /**
     * 基于IP的queue模板
     *
     * @param queueName
     * @return
     */
    private static String ipTemplate(String queueName) {
        return String.format("%s %s", queueName, getIp());
    }

    private static String getIp() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

}