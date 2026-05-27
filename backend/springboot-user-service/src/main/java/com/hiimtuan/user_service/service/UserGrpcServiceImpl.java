package com.hiimtuan.user_service.service;

import com.google.protobuf.Empty;
import com.hiimtuan.common_service.constant.RoleEnum;
import com.hiimtuan.common_service.service.JwtService;
import com.hiimtuan.user_service.config.GrpcJwtInterceptor;
import com.hiimtuan.user_service.entity.User;
import com.hiimtuan.user_service.repository.UserRepository;
import com.hiimtuan.user_service.proto.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserGrpcServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void getUser(Empty request, StreamObserver<ResponseUser> responseObserver) {
        String jwt = GrpcJwtInterceptor.JWT_CTX_KEY.get();
        if (jwt == null) {
            responseObserver.onError(Status.UNAUTHENTICATED
                    .withDescription("Missing authorization token").asException());
            return;
        }
        try {
            String userId = jwtService.extractUsername(jwt);
            User user = userRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new RuntimeException("User not found"));
            responseObserver.onNext(ResponseUser.newBuilder().setUser(toGrpcUser(user)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asException());
        }
    }

    @Override
    public void validateCredentials(ValidateCredentialsRequest request,
                                    StreamObserver<ValidateCredentialsResponse> responseObserver) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
            if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
                responseObserver.onNext(ValidateCredentialsResponse.newBuilder()
                        .setSuccess(false)
                        .setError("Invalid email or password")
                        .build());
            } else {
                responseObserver.onNext(ValidateCredentialsResponse.newBuilder()
                        .setSuccess(true)
                        .setUser(toGrpcUser(userOpt.get()))
                        .build());
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asException());
        }
    }

    @Override
    public void getUserByEmail(GetUserByEmailRequest request,
                               StreamObserver<ResponseUser> responseObserver) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            responseObserver.onNext(ResponseUser.newBuilder().setUser(toGrpcUser(user)).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asException());
        }
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request,
                               StreamObserver<UpdatePasswordResponse> responseObserver) {
        try {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            responseObserver.onNext(UpdatePasswordResponse.newBuilder().setSuccess(true).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asException());
        }
    }

    private GrpcUser toGrpcUser(User user) {
        List<String> roles = switch (user.getRole().getName()) {
            case USER -> List.of("ROLE_USER");
            case ADMIN -> List.of("ROLE_USER", "ROLE_ADMIN");
            case SUPER_ADMIN -> List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
        };
        return GrpcUser.newBuilder()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setFullName(user.getFullName())
                .addAllRoles(roles)
                .build();
    }
}
