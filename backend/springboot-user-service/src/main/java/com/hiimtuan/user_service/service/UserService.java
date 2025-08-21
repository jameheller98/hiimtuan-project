package com.hiimtuan.user_service.service;

import com.hiimtuan.user_service.dto.request.*;
import com.hiimtuan.user_service.dto.response.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    RegisterResponseDto register(RegisterRequestDto registerRequestDto);
    RegisterResponseDto registerAdmin(RegisterRequestDto registerRequestDto);
    List<UserResponseDto> allUsers(Pageable pageable);
}
