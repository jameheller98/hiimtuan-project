package com.hiimtuan.api_gateway.service;

import com.hiimtuan.api_gateway.entity.RefreshToken;
import com.hiimtuan.api_gateway.entity.User;
import com.hiimtuan.api_gateway.repository.RefreshTokenRepository;
import com.hiimtuan.common_service.config.AuthenticationConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(User user) {
        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByUserId(user.getId());

        oldRefreshToken.ifPresent(refreshTokenRepository::delete);

        RefreshToken token =  RefreshToken.builder()
                .user(user)
                .expiryDate(Instant.now().plusMillis(authenticationConfiguration.getRefreshExpirationTime()))
                .token(UUID.randomUUID().toString())
                .build();

        return refreshTokenRepository.save(token);
    }

    @Override
    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
}
