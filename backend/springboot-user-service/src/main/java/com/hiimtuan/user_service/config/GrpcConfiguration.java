package com.hiimtuan.user_service.config;

import com.hiimtuan.mail_service.proto.MailServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfiguration {
    @Bean
    MailServiceGrpc.MailServiceBlockingStub stubMailService(GrpcChannelFactory channels) {
        return MailServiceGrpc.newBlockingStub(channels.createChannel("local"));
    }
}
