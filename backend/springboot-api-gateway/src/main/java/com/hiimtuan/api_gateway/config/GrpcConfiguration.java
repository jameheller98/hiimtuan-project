package com.hiimtuan.api_gateway.config;

import com.hiimtuan.mail_service.proto.MailServiceGrpc;
import com.hiimtuan.user_service.proto.UserServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfiguration {
    @Bean
    MailServiceGrpc.MailServiceBlockingStub stubMailService(GrpcChannelFactory channels) {
        return MailServiceGrpc.newBlockingStub(channels.createChannel("mail-service"));
    }

    @Bean
    UserServiceGrpc.UserServiceBlockingStub stubUserService(GrpcChannelFactory channels) {
        return UserServiceGrpc.newBlockingStub(channels.createChannel("user-service"));
    }
}
