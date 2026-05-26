package com.hiimtuan.api_gateway.service;

import com.hiimtuan.api_gateway.entity.PasswordResetToken;
import com.hiimtuan.api_gateway.entity.User;

public interface PasswordResetTokenService {
    PasswordResetToken createPasswordResetToken(User user);
    boolean isTokenExpired(PasswordResetToken token);
}
