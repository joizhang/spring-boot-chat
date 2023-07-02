package com.joizhang.chat.web.api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageContentSubtype {

    DEFAULT(0, "Error"),
    ;

    private final Integer type;

    private final String description;

}
