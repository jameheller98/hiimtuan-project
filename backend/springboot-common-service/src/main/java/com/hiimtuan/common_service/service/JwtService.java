package com.hiimtuan.common_service.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
     String generateToken(String key);
     boolean isTokenValid(String token, String key);
     String extractUsername(String token);
}
