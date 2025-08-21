package com.hiimtuan.api_gateway.exception.custom;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String message) {
        super(message);
    }
}
