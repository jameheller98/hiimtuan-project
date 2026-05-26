package com.hiimtuan.api_gateway.dto.response;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private String refreshToken;
}
