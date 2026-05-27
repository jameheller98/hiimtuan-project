package com.hiimtuan.mail_service.controller;

import com.hiimtuan.mail_service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hiimtuan.mail_service.dto.request.MailRequestDto;
import com.hiimtuan.mail_service.service.MailService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RequestMapping("/api/v1/mail")
@RestController
@AllArgsConstructor
public class MailController {
	private MailService mailService;

	@PostMapping("/send")
	public ResponseEntity<ApiResponse<String>> send(@Valid @RequestBody MailRequestDto mailRequestDto) {
		mailService.sendSimpleMail(mailRequestDto);

		return ResponseEntity.ok(ApiResponse.success("Send mail success!",null));
	}
}
