package com.barlow.client.ai.openai.api;

import com.barlow.client.ai.openai.api.exception.OpenAiApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class OpenAiErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        return new OpenAiApiException(response.status(), response.reason());
    }
}
