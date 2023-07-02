package com.joizhang.chat.web.config;

import com.joizhang.chat.web.api.constant.RabbitConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->
                log.debug("消息发送成功: correlationData({}), ack({}), cause({})",
                        correlationData, ack, cause)
        );
        rabbitTemplate.setReturnsCallback(returned -> log.error(
                "消息丢失: exchange({}), route({}), replyCode({}), replyText({}), message:{}",
                returned.getExchange(),
                returned.getRoutingKey(),
                returned.getReplyCode(),
                returned.getReplyText(),
                returned.getMessage()
        ));
        return rabbitTemplate;
    }

    /**
     * 工作队列模式，消息通过轮训的方式发送给一个消费者
     */
    @Bean
    public Queue messagePersistenceQueue() {
        return new Queue(RabbitConstants.QUEUE_WORK_MESSAGE_PERSISTENCE, true, false, false);
    }
}
