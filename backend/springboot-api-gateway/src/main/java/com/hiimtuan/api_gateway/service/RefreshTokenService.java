package com.hiimtuan.api_gateway.service;

import com.hiimtuan.api_gateway.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);
    boolean isTokenExpired(RefreshToken token);
}
