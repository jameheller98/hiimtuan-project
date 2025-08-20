package com.hiimtuan.common_service.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("security.jwt")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationConfiguration {
    private long expirationTime;
    private long refreshExpirationTime;
    private long passwordResetExpirationTime;
    private String secretKey;
}
