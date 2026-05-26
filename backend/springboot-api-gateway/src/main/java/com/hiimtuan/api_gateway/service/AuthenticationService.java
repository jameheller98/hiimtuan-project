package com.hiimtuan.api_gateway.service;

import com.hiimtuan.api_gateway.dto.request.*;
import com.hiimtuan.api_gateway.dto.response.*;

public interface AuthenticationService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    LoginResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
    String resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);
    String changePassword(ChangePasswordRequestDto changePasswordRequestDto);
    String logout();
}
