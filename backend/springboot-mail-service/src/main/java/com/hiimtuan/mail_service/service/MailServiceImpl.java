package com.hiimtuan.mail_service.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.hiimtuan.mail_service.dto.request.MailRequestDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {
	private JavaMailSender javaMailSender;

	@Override
	@Async
	public void sendSimpleMail(MailRequestDto mailRequestDto) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();

		mailMessage.setTo(mailRequestDto.getRecipient());
		mailMessage.setText(mailRequestDto.getMsgBody());
		mailMessage.setSubject(mailRequestDto.getSubject());

		javaMailSender.send(mailMessage);
	}

}
