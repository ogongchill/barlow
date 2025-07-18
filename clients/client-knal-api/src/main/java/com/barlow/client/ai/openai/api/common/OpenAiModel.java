package com.barlow.client.ai.openai.api.common;

import com.barlow.client.ai.openai.api.exception.OpenAiInternalException;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum OpenAiModel {

    GPT_4_O_MINI("gpt-4o-mini"),
    GPT_4_1_NANO("gpt-4.1-nano"),
    GPT_4_1_MINI("gpt-4.1-mini")
    ;

    private final String value;

    OpenAiModel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public OpenAiModel findByValue(String target) {
        return Arrays.stream(values())
                .filter(model -> model.value.equals(target))
                .findFirst()
                .orElseThrow(() -> new OpenAiInternalException("no such model name:" + target));
    }
}
