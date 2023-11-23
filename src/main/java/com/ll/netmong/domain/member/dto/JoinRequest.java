package com.ll.netmong.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JoinRequest {

	@NotBlank
	@Size(min = 4, max = 40)
	private String username;

	@NotBlank
	@Size(min = 6, max = 20)
	private String password;

	@NotBlank
	@Size(min = 3, max = 15)
	private String realname;

	@NotBlank
	@Size(max = 40)
	@Email
	private String email;
}
