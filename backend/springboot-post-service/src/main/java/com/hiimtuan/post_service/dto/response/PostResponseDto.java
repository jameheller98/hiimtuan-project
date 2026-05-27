package com.hiimtuan.post_service.dto.response;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;
    private String authorEmail;
    private Date createdAt;
    private Date updatedAt;
}
