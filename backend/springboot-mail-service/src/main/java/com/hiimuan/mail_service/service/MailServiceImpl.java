package com.hiimuan.mail_service.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hiimuan.mail_service.dto.request.MailRequestDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {
	private JavaMailSender javaMailSender;

	@Override
	public String sendSimpleMail(MailRequestDto mailRequestDto) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			mailMessage.setTo(mailRequestDto.getRecipient());
			mailMessage.setText(mailRequestDto.getMsgBody());
			mailMessage.setSubject(mailRequestDto.getSubject());

			javaMailSender.send(mailMessage);

			return "Mail Sent Successfully...";
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while Sending Mail";
		}
	}

}
