package com.hiimtuan.mail_service.service;

import com.hiimtuan.mail_service.dto.request.MailRequestDto;

public interface MailService {
	String sendSimpleMail(MailRequestDto mailRequestDto);
}
