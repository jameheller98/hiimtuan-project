package com.hiimtuan.mail_service.service;

import com.google.protobuf.Empty;
import com.hiimtuan.mail_service.dto.request.MailRequestDto;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.hiimtuan.mail_service.proto.MailServiceGrpc;
import com.hiimtuan.mail_service.proto.RequestMail;

@Service
@AllArgsConstructor
public class MailServiceGrpcImpl extends MailServiceGrpc.MailServiceImplBase {
    private MailService mailService;

    @Override
    public void sendSimpleMail(RequestMail req, StreamObserver<Empty> responseObserver) {
        MailRequestDto mailRequestDto = MailRequestDto.builder()
                .recipient(req.getRecipient())
                .msgBody(req.getMsgBody())
                .subject(req.getSubject())
                .build();

        mailService.sendSimpleMail(mailRequestDto);

        Empty empty =  Empty.newBuilder().build();

        responseObserver.onNext(empty);
        responseObserver.onCompleted();
    }
}
