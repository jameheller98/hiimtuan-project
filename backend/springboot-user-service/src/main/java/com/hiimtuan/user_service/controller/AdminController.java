package com.hiimtuan.user_service.controller;

import com.hiimtuan.user_service.dto.request.*;
import com.hiimtuan.user_service.dto.response.*;
import com.hiimtuan.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin")
@RestController
@AllArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> createAdministrator(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(userService.registerAdmin(registerRequestDto).getMessage(), null));
    }
}
