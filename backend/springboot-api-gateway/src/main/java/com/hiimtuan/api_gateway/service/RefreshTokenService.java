package com.hiimtuan.api_gateway.service;

import com.hiimtuan.api_gateway.entity.RefreshToken;
import com.hiimtuan.api_gateway.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    boolean isTokenExpired(RefreshToken token);
}
