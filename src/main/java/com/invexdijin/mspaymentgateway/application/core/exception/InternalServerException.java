package com.invexdijin.mspaymentgateway.application.core.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}
