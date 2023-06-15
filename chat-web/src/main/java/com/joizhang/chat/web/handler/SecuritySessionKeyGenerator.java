package com.joizhang.chat.web.handler;

import cn.hutool.core.util.ObjectUtil;
import com.joizhang.chat.web.api.constant.ChatConstants;
import org.springframework.web.socket.WebSocketSession;

public class SecuritySessionKeyGenerator implements SessionKeyGenerator {

    @Override
    public String sessionKey(WebSocketSession webSocketSession) {
        Object obj = webSocketSession.getAttributes().get(ChatConstants.USER_KEY_ATTR_ID);
        if (ObjectUtil.isNotNull(obj)) {
            return obj.toString();
        }
        return "";
    }
}
