package com.hiimtuan.api_gateway.service;

import com.hiimtuan.api_gateway.entity.PasswordResetToken;

public interface PasswordResetTokenService {
    PasswordResetToken createPasswordResetToken(Long userId);
    boolean isTokenExpired(PasswordResetToken token);
}
