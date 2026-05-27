package com.hiimtuan.post_service.config;

import com.hiimtuan.user_service.proto.UserServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfiguration {
    @Bean
    UserServiceGrpc.UserServiceBlockingStub stubUserService(GrpcChannelFactory channels) {
        return UserServiceGrpc.newBlockingStub(channels.createChannel("user-service"));
    }
}
