package com.hiimtuan.mail_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MailRequestDto {
	@NotBlank(message = "recipient is required")
	@Email(message = "recipient is invalid")
	private String recipient;

	@NotBlank(message = "msgBody is required")
	private String msgBody;

	@NotBlank(message = "subject is required")
	private String subject;
}
