package com.hiimtuan.mail_service.service;

import com.hiimtuan.mail_service.dto.request.MailRequestDto;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.hiimtuan.mail_service.proto.MailServiceGrpc;
import com.hiimtuan.mail_service.proto.RequestMail;
import com.hiimtuan.mail_service.proto.ResponseMail;

@Service
@AllArgsConstructor
public class MailServiceGrpcImpl extends MailServiceGrpc.MailServiceImplBase {
    private MailService mailService;

    @Override
    public void sendSimpleMail(RequestMail req, StreamObserver<ResponseMail> responseObserver) {
        MailRequestDto mailRequestDto = MailRequestDto.builder()
                .recipient(req.getRecipient())
                .msgBody(req.getMsgBody())
                .subject(req.getSubject())
                .build();

        mailService.sendSimpleMail(mailRequestDto);

        ResponseMail reply = ResponseMail.newBuilder().setMessage("Send mail success!").build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
