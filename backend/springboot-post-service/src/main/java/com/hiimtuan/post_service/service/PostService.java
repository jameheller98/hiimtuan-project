package com.hiimtuan.post_service.service;

import com.hiimtuan.post_service.dto.request.CreatePostRequestDto;
import com.hiimtuan.post_service.dto.response.PostResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(CreatePostRequestDto request, String jwt);
    PostResponseDto getPost(Long id);
    List<PostResponseDto> getAllPosts(Pageable pageable);
    List<PostResponseDto> getMyPosts(String jwt, Pageable pageable);
    PostResponseDto updatePost(Long id, CreatePostRequestDto request, String jwt);
    void deletePost(Long id, String jwt);
}
