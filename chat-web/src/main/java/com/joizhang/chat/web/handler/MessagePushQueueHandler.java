package com.joizhang.chat.web.handler;

import cn.hutool.json.JSONUtil;
import com.joizhang.chat.web.api.constant.RabbitConstants;
import com.joizhang.chat.web.api.entity.ChatMessage;
import com.joizhang.chat.web.service.ChatMessageService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue,
                exchange = @Exchange(value = RabbitConstants.EXCHANGE_FANOUT_MESSAGE, type = ExchangeTypes.FANOUT)
        )
)
public class MessagePushQueueHandler {

    private final ChatMessageService messageService;

    @RabbitHandler
    public void messageMQHandlerManualAck(ChatMessage chatMessage, Message message, Channel channel) {
        try {
            messageService.pushToReceiver(chatMessage);
            log.debug(
                    "交换机：{}，手动ACK，接收消息：{}",
                    RabbitConstants.EXCHANGE_FANOUT_MESSAGE,
                    JSONUtil.toJsonStr(chatMessage)
            );
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            // 通知 MQ 消息已被成功消费,可以ACK了
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
