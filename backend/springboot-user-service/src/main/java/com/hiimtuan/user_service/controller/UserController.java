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
    public ResponseEntity<ApiResponse<UserResponseDto>> authenticatedUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(ApiResponse.success("Get user profile success!", userMapper.UserToUserResponse(null)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> allUsers(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Get list user success!", userService.allUsers(pageable)));
    }
}