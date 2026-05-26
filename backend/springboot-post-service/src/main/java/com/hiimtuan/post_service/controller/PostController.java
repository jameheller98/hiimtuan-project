package com.hiimtuan.post_service.controller;

import com.google.protobuf.Empty;
import com.hiimtuan.authentication_service.proto.AuthenticationServiceGrpc;
import com.hiimtuan.authentication_service.proto.ResponseUser;
import com.hiimtuan.post_service.dto.response.ApiResponse;
import com.hiimtuan.post_service.entity.User;
import com.hiimtuan.post_service.mapper.UserMapper;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequestMapping("/api/v1/post")
@RestController
@AllArgsConstructor
public class PostController {
    private final AuthenticationServiceGrpc.AuthenticationServiceBlockingStub stubAuthenticationService;
    private final UserMapper userMapper;

    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<User>> ping(){
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Metadata metadata = new Metadata();

        if(sra != null){
            HttpServletRequest request = sra.getRequest();
            String jwtToken = request.getHeader("Authorization");

            metadata.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), jwtToken);
        }

        ResponseUser responseUser = stubAuthenticationService.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata)).getUser(Empty.getDefaultInstance());

        return ResponseEntity.ok(ApiResponse.success("Ping success", userMapper.GrpcUserToUser( responseUser.getUser())));
    }
}
