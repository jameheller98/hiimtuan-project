package com.hiimtuan.api_gateway.service;

import com.hiimtuan.api_gateway.dto.response.*;
import com.hiimtuan.api_gateway.dto.request.*;
import com.hiimtuan.api_gateway.entity.PasswordResetToken;
import com.hiimtuan.api_gateway.entity.RefreshToken;
import com.hiimtuan.api_gateway.entity.User;
import com.hiimtuan.api_gateway.mapper.UserMapper;
import com.hiimtuan.api_gateway.repository.PasswordResetTokenRepository;
import com.hiimtuan.api_gateway.repository.RefreshTokenRepository;
import com.hiimtuan.api_gateway.repository.UserRepository;
import com.hiimtuan.common_service.service.JwtService;
import com.hiimtuan.mail_service.proto.MailServiceGrpc;
import com.hiimtuan.mail_service.proto.RequestMail;
import com.hiimtuan.api_gateway.exception.custom.AccountException;
import com.hiimtuan.api_gateway.exception.custom.PasswordResetTokenException;
import com.hiimtuan.api_gateway.exception.custom.RefreshTokenException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl  implements AuthenticationService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final MailServiceGrpc.MailServiceBlockingStub stubMailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Optional<User> authenticatedUser = userRepository.findByEmail(loginRequestDto.getEmail());
        
        if(authenticatedUser.isEmpty()) {
            throw new AccountException("User not found");
        }

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), authenticatedUser.get().getPassword())) {
            throw new AccountException("Password is incorrect");
        }

        String jwtToken = jwtService.generateToken(authenticatedUser.get().getId().toString());

        RefreshToken jwtRefreshToken = refreshTokenService.createRefreshToken(authenticatedUser.get());

        return userMapper.UserToLoginResponse(jwtToken, jwtRefreshToken.getToken());
    }

    @Override
    public LoginResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        Optional<RefreshToken> currentRefreshToken = refreshTokenRepository.findByToken(refreshTokenRequestDto.getRefreshToken());

        if (currentRefreshToken.isEmpty()){
            throw new RefreshTokenException("Invalid refresh token.");
        }

        if (refreshTokenService.isTokenExpired(currentRefreshToken.get())){
            refreshTokenRepository.delete(currentRefreshToken.get());
            throw new RefreshTokenException("Refresh token expired. Please login again.");
        }

        if (currentRefreshToken.get().getUser() == null) {
            throw new AccountException("User not found");
        }

        String jwtToken = jwtService.generateToken(currentRefreshToken.get().getUser().getId().toString());

        RefreshToken jwtRefreshToken = refreshTokenService.createRefreshToken(currentRefreshToken.get().getUser());

        return userMapper.UserToLoginResponse(jwtToken, jwtRefreshToken.getToken());
    }

    @Override
    public String resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        Optional<User> user = userRepository.findByEmail(resetPasswordRequestDto.getEmail());

        if (user.isEmpty()) {
            throw new AccountException("User not found");
        }

        PasswordResetToken jwtPasswordRefreshToken = passwordResetTokenService.createPasswordResetToken(user.get());

        RequestMail requestMail = RequestMail.newBuilder()
                .setRecipient(resetPasswordRequestDto.getEmail())
                .setMsgBody(String.format("This is link change password:%nhttp://localhost:3000/auth/change-password?token=%s%nPlease not share link!", jwtPasswordRefreshToken.getToken()))
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

        User user = currentPasswordResetToken.get().getUser();

        if (user == null) {
            throw new AccountException("User not found");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getPassword()));

        userRepository.save(user);

        passwordResetTokenRepository.delete(currentPasswordResetToken.get());

        return "Change password success!";
    }

    @Override
    public String logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if(authentication.getPrincipal() instanceof User currentUser){
                Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(currentUser.getId());

                refreshToken.ifPresent(refreshTokenRepository::delete);
            }
        }

        SecurityContextHolder.clearContext();

        return "Logout success!";
    }
}