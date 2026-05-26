package com.hiimtuan.post_service.config;

import com.hiimtuan.authentication_service.proto.AuthenticationServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfiguration {
    @Bean
    AuthenticationServiceGrpc.AuthenticationServiceBlockingStub stubMailService(GrpcChannelFactory channels) {
        return AuthenticationServiceGrpc.newBlockingStub(channels.createChannel("local"));
    }
}
