package com.hiimtuan.api_gateway.service;

import com.hiimtuan.api_gateway.dto.response.*;
import com.hiimtuan.api_gateway.dto.request.*;
import com.hiimtuan.api_gateway.entity.PasswordResetToken;
import com.hiimtuan.api_gateway.entity.RefreshToken;
import com.hiimtuan.api_gateway.repository.PasswordResetTokenRepository;
import com.hiimtuan.api_gateway.repository.RefreshTokenRepository;
import com.hiimtuan.common_service.service.JwtService;
import com.hiimtuan.mail_service.proto.MailServiceGrpc;
import com.hiimtuan.mail_service.proto.RequestMail;
import com.hiimtuan.user_service.proto.*;
import com.hiimtuan.api_gateway.exception.custom.AccountException;
import com.hiimtuan.api_gateway.exception.custom.PasswordResetTokenException;
import com.hiimtuan.api_gateway.exception.custom.RefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.frontend-url:http://localhost:3000}")
    private String frontendUrl;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final MailServiceGrpc.MailServiceBlockingStub stubMailService;
    private final UserServiceGrpc.UserServiceBlockingStub stubUserService;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        ValidateCredentialsResponse response = stubUserService.validateCredentials(
                ValidateCredentialsRequest.newBuilder()
                        .setEmail(loginRequestDto.getEmail())
                        .setPassword(loginRequestDto.getPassword())
                        .build()
        );

        if (!response.getSuccess()) {
            throw new AccountException(response.getError());
        }

        long userId = response.getUser().getId();
        String jwtToken = jwtService.generateToken(String.valueOf(userId));
        RefreshToken jwtRefreshToken = refreshTokenService.createRefreshToken(userId);

        return LoginResponseDto.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken.getToken())
                .build();
    }

    @Override
    public LoginResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        Optional<RefreshToken> currentRefreshToken = refreshTokenRepository.findByToken(refreshTokenRequestDto.getRefreshToken());

        if (currentRefreshToken.isEmpty()) {
            throw new RefreshTokenException("Invalid refresh token.");
        }

        if (refreshTokenService.isTokenExpired(currentRefreshToken.get())) {
            refreshTokenRepository.delete(currentRefreshToken.get());
            throw new RefreshTokenException("Refresh token expired. Please login again.");
        }

        long userId = currentRefreshToken.get().getUserId();
        String jwtToken = jwtService.generateToken(String.valueOf(userId));
        RefreshToken jwtRefreshToken = refreshTokenService.createRefreshToken(userId);

        return LoginResponseDto.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken.getToken())
                .build();
    }

    @Override
    public String resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        ResponseUser responseUser;
        try {
            responseUser = stubUserService.getUserByEmail(
                    GetUserByEmailRequest.newBuilder().setEmail(resetPasswordRequestDto.getEmail()).build()
            );
        } catch (Exception e) {
            throw new AccountException("User not found");
        }

        long userId = responseUser.getUser().getId();
        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(userId);

        RequestMail requestMail = RequestMail.newBuilder()
                .setRecipient(resetPasswordRequestDto.getEmail())
                .setMsgBody(String.format("This is link change password:%n%s/auth/change-password?token=%s%nPlease not share link!", frontendUrl, passwordResetToken.getToken()))
                .setSubject("Change password")
                .build();

        stubMailService.sendSimpleMail(requestMail);

        return String.format("Check mail(%s) to get link change password.", resetPasswordRequestDto.getEmail());
    }

    @Override
    public String changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        Optional<PasswordResetToken> currentPasswordResetToken = passwordResetTokenRepository.findByToken(changePasswordRequestDto.getPasswordResetToken());

        if (currentPasswordResetToken.isEmpty()) {
            throw new PasswordResetTokenException("Invalid password reset token.");
        }

        if (passwordResetTokenService.isTokenExpired(currentPasswordResetToken.get())) {
            passwordResetTokenRepository.delete(currentPasswordResetToken.get());
            throw new PasswordResetTokenException("Password reset token expired");
        }

        long userId = currentPasswordResetToken.get().getUserId();

        stubUserService.updatePassword(
                UpdatePasswordRequest.newBuilder()
                        .setUserId(userId)
                        .setNewPassword(changePasswordRequestDto.getPassword())
                        .build()
        );

        passwordResetTokenRepository.delete(currentPasswordResetToken.get());

        return "Change password success!";
    }

    @Override
    public String logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof String userId) {
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(Long.valueOf(userId));
            refreshToken.ifPresent(refreshTokenRepository::delete);
        }

        SecurityContextHolder.clearContext();

        return "Logout success!";
    }
}