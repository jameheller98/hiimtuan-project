package com.hiimtuan.user_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequestDto {
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;
    @NotBlank
    @Size(min = 6)
    private String password;
    @NotBlank
    private String fullName;
}