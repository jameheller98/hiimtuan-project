package com.hiimtuan.common_service.service;

public interface JwtService {
     String generateToken(String key);
     boolean isTokenValid(String token, String key);
     String extractUsername(String token);
}
