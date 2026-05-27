package com.hiimtuan.post_service.controller;

import com.hiimtuan.post_service.dto.request.CreatePostRequestDto;
import com.hiimtuan.post_service.dto.response.ApiResponse;
import com.hiimtuan.post_service.dto.response.PostResponseDto;
import com.hiimtuan.post_service.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/post")
@RestController
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(
            @Valid @RequestBody CreatePostRequestDto request,
            @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.substring(7);
        return ResponseEntity.ok(ApiResponse.success("Create post success!", postService.createPost(request, jwt)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Get post success!", postService.getPost(id)));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getAllPosts(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Get posts success!", postService.getAllPosts(pageable)));
    }

    @GetMapping("/my-posts")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getMyPosts(
            @RequestHeader("Authorization") String authHeader, Pageable pageable) {
        String jwt = authHeader.substring(7);
        return ResponseEntity.ok(ApiResponse.success("Get my posts success!", postService.getMyPosts(jwt, pageable)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody CreatePostRequestDto request,
            @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.substring(7);
        return ResponseEntity.ok(ApiResponse.success("Update post success!", postService.updatePost(id, request, jwt)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.substring(7);
        postService.deletePost(id, jwt);
        return ResponseEntity.ok(ApiResponse.success("Delete post success!", null));
    }
}
