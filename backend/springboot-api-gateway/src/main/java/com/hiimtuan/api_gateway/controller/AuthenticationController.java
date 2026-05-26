package com.hiimtuan.api_gateway.controller;

import com.hiimtuan.api_gateway.dto.request.*;
import com.hiimtuan.api_gateway.dto.response.*;
import com.hiimtuan.api_gateway.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth")
@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(ApiResponse.success("Login success!", authenticationService.login(loginRequestDto)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDto>> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        return ResponseEntity.ok(ApiResponse.success("Refresh token success!", authenticationService.refreshToken(refreshTokenRequestDto)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(authenticationService.resetPassword(resetPasswordRequestDto), null));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(authenticationService.changePassword(changePasswordRequestDto), null));
    }

    @GetMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(ApiResponse.success(authenticationService.logout(), null));
    }
}
