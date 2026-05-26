package com.hiimtuan.user_service.controller;

import com.hiimtuan.user_service.dto.response.*;
import com.hiimtuan.user_service.dto.request.*;
import com.hiimtuan.user_service.mapper.UserMapper;
import com.hiimtuan.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/user")
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(userService.register(registerRequestDto).getMessage(), null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> authenticatedUser(@RequestHeader("Authorization") String authHeader) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            return ResponseEntity.ok(ApiResponse.success("Get user profile success!", userService.getProfile(token)));
        }
       

        return ResponseEntity.ok(ApiResponse.error("Missing or invalid Authorization token!", userMapper.UserToUserResponse(null)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> allUsers(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Get list user success!", userService.allUsers(pageable)));
    }
}