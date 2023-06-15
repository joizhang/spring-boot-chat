package com.joizhang.chat.web.api.constant;

import cn.hutool.core.util.IdUtil;

public interface RabbitConstants {

    public static final String QUEUE_ID = IdUtil.randomUUID();

    /**
     * fanout 交换机
     */
    String EXCHANGE_FANOUT_MESSAGE = "exchange.fanout.message";

    /**
     * 消息推送队列
     */
    public static final String QUEUE_PUBSUB_MESSAGE_PUSH = "queue.pubsub.message.push";

    /**
     * 消息持久化队列
     */
    String QUEUE_WORK_MESSAGE_PERSISTENCE = "queue.work.message.persistence";

}
