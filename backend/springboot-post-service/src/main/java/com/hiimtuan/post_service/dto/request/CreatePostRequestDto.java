package com.hiimtuan.post_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreatePostRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
