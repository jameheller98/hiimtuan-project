package com.hiimtuan.mail_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiimtuan.mail_service.dto.request.MailRequestDto;
import com.hiimtuan.mail_service.service.MailService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RequestMapping("/mail")
@RestController
@AllArgsConstructor
public class MailController {
	private MailService mailService;

	@PostMapping("/send")
	public ResponseEntity<String> send(@Valid @RequestBody MailRequestDto mailRequestDto) {
		return ResponseEntity.ok(mailService.sendSimpleMail(mailRequestDto));
	}
}
