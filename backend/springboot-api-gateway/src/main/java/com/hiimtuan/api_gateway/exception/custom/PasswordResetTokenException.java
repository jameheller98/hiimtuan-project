package com.hiimtuan.api_gateway.exception.custom;

public class PasswordResetTokenException extends RuntimeException {
    public PasswordResetTokenException(String message) {
        super(message);
    }
}
