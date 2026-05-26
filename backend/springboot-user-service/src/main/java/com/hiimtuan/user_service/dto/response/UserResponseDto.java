package com.hiimtuan.user_service.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserResponseDto {
    Long id;
    String fullName;
    String email;
    List<String> roles;
}
