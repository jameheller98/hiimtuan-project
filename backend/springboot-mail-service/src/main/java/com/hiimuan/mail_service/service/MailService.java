package com.hiimuan.mail_service.service;

import com.hiimuan.mail_service.dto.request.MailRequestDto;

public interface MailService {
	String sendSimpleMail(MailRequestDto mailRequestDto);
}
