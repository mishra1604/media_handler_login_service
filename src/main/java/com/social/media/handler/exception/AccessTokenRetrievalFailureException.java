package com.social.media.handler.exception;

public class AccessTokenRetrievalFailureException extends RuntimeException {

    public AccessTokenRetrievalFailureException(String message) {
        super(message);
    }
}
