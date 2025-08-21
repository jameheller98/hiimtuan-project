package com.hiimtuan.api_gateway.mapper;

import com.hiimtuan.api_gateway.dto.response.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper {

    public LoginResponseDto UserToLoginResponse(String token, String refreshToken) {
        return LoginResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
