package com.joizhang.chat.web.handler;

import cn.hutool.json.JSONUtil;
import com.joizhang.chat.web.api.constant.RabbitConstants;
import com.joizhang.chat.web.api.entity.ChatMessage;
import com.joizhang.chat.web.service.ChatMessageService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@RabbitListener(queues = RabbitConstants.QUEUE_WORK_MESSAGE_PERSISTENCE)
public class MessagePersistenceQueueHandler {

    private final ChatMessageService messageService;

    @RabbitHandler
    public void persistenceMQHandlerManualAck(ChatMessage chatMessage, Message message, Channel channel) {
        try {
            messageService.saveAndAck(chatMessage);
            log.debug(
                    "队列：{}，手动ACK，接收消息：{}",
                    RabbitConstants.QUEUE_WORK_MESSAGE_PERSISTENCE,
                    JSONUtil.toJsonStr(chatMessage)
            );
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            // 通知 MQ 消息已被成功消费,可以ACK了
            channel.basicAck(deliveryTag, false);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            // TODO 通知发送者消息传递失败
        }
    }

}
