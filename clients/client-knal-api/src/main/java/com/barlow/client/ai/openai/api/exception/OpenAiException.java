package com.barlow.client.ai.openai.api.exception;

public abstract class OpenAiException extends RuntimeException {

    protected OpenAiException(String message) {
        super(message);
    }
}
