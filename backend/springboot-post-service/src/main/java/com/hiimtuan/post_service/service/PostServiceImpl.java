package com.hiimtuan.post_service.service;

import com.google.protobuf.Empty;
import com.hiimtuan.user_service.proto.UserServiceGrpc;
import com.hiimtuan.user_service.proto.GrpcUser;
import com.hiimtuan.user_service.proto.ResponseUser;
import com.hiimtuan.post_service.dto.request.CreatePostRequestDto;
import com.hiimtuan.post_service.dto.response.PostResponseDto;
import com.hiimtuan.post_service.entity.Post;
import com.hiimtuan.post_service.repository.PostRepository;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserServiceGrpc.UserServiceBlockingStub stubUserService;

    @Override
    public PostResponseDto createPost(CreatePostRequestDto request, String jwt) {
        GrpcUser grpcUser = fetchUser(jwt);
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .authorId(grpcUser.getId())
                .build();
        return toDto(postRepository.save(post), grpcUser);
    }

    @Override
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return toDto(post, null);
    }

    @Override
    public List<PostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).stream()
                .map(p -> toDto(p, null))
                .toList();
    }

    @Override
    public List<PostResponseDto> getMyPosts(String jwt, Pageable pageable) {
        GrpcUser grpcUser = fetchUser(jwt);
        return postRepository.findByAuthorId(grpcUser.getId(), pageable).stream()
                .map(p -> toDto(p, grpcUser))
                .toList();
    }

    @Override
    public PostResponseDto updatePost(Long id, CreatePostRequestDto request, String jwt) {
        GrpcUser grpcUser = fetchUser(jwt);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getAuthorId().equals(grpcUser.getId())) {
            throw new RuntimeException("You are not the author of this post");
        }
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        return toDto(postRepository.save(post), grpcUser);
    }

    @Override
    public void deletePost(Long id, String jwt) {
        GrpcUser grpcUser = fetchUser(jwt);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!post.getAuthorId().equals(grpcUser.getId())) {
            throw new RuntimeException("You are not the author of this post");
        }
        postRepository.delete(post);
    }

    private GrpcUser fetchUser(String jwt) {
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer " + jwt);
        ResponseUser responseUser = stubUserService
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata))
                .getUser(Empty.getDefaultInstance());
        return responseUser.getUser();
    }

    private PostResponseDto toDto(Post post, GrpcUser author) {
        PostResponseDto dto = PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(post.getAuthorId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
        if (author != null) {
            dto.setAuthorName(author.getFullName());
            dto.setAuthorEmail(author.getEmail());
        }
        return dto;
    }
}
