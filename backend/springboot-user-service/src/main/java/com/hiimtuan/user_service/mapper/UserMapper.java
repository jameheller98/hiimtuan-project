package com.hiimtuan.user_service.mapper;

import com.hiimtuan.user_service.dto.request.RegisterRequestDto;
import com.hiimtuan.user_service.dto.response.RegisterResponseDto;
import com.hiimtuan.user_service.dto.response.UserResponseDto;
import com.hiimtuan.user_service.entity.Role;
import com.hiimtuan.user_service.entity.User;
import com.hiimtuan.common_service.constant.RoleEnum;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User RegisterRequestToUser(RegisterRequestDto registerUserDto, Role role) {
        return User.builder()
                .fullName(registerUserDto.getFullName())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .role(role)
                .build();
    }

    public RegisterResponseDto UserToRegisterResponse() {
        return RegisterResponseDto.builder()
                .message("Woohoo! You're officially part of the my family.")
                .build();
    }

    public UserResponseDto UserToUserResponse(User user) {
        Role role = user.getRole();
        List<String> roles = null;

        if(role.getName().equals(RoleEnum.USER)) {
            roles = List.of("ROLE_" + RoleEnum.USER);
        }

        if(role.getName().equals(RoleEnum.ADMIN)) {
            roles = List.of("ROLE_" + RoleEnum.USER, "ROLE_" + RoleEnum.ADMIN);
        }

        if(role.getName().equals(RoleEnum.SUPER_ADMIN)) {
            roles = List.of("ROLE_" + RoleEnum.USER, "ROLE_" + RoleEnum.ADMIN, "ROLE_" + RoleEnum.SUPER_ADMIN);
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }
}
