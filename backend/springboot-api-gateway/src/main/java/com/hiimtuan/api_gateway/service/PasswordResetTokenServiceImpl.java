package com.hiimtuan.api_gateway.service;

import com.hiimtuan.api_gateway.entity.PasswordResetToken;
import com.hiimtuan.api_gateway.entity.User;
import com.hiimtuan.api_gateway.repository.PasswordResetTokenRepository;
import com.hiimtuan.common_service.config.AuthenticationConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken createPasswordResetToken(User user) {
        Optional<PasswordResetToken> oldPasswordResetToken = passwordResetTokenRepository.findByUserId(user.getId());

        oldPasswordResetToken.ifPresent(passwordResetTokenRepository::delete);

        PasswordResetToken token = PasswordResetToken.builder()
                .user(user)
                .expiryDate(Instant.now().plusMillis(authenticationConfiguration.getPasswordResetExpirationTime()))
                .token(UUID.randomUUID().toString())
                .build();

        return passwordResetTokenRepository.save(token);
    }

    @Override
    public boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
}
