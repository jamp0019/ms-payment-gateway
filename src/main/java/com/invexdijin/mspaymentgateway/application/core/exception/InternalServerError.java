package com.invexdijin.mspaymentgateway.application.core.exception;

public class InternalServerError extends RuntimeException {
    public InternalServerError(String message) {
        super(message);
    }
}
