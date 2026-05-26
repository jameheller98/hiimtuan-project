package com.hiimtuan.api_gateway.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RefreshTokenRequestDto {
    private String refreshToken;
}
