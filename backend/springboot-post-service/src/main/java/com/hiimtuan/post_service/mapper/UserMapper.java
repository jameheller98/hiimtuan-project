package com.hiimtuan.post_service.mapper;

import com.hiimtuan.authentication_service.proto.GrpcUser;
import com.hiimtuan.post_service.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper {
    public User GrpcUserToUser(GrpcUser grpcUser) {
        return User.builder()
                .id(grpcUser.getId())
                .fullName(grpcUser.getFullName())
                .email(grpcUser.getEmail())
                .build();
    }
}
