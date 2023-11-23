package com.ll.netmong.domain.member.entity;

import com.ll.netmong.domain.member.exception.NotMatchAuthLevelException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AuthLevel {
	MEMBER(3, "ROLE_USER"),
	ADMIN(7, "ROLE_ADMIN");

	private final Integer code;
	private final String value;

	public static AuthLevel codeOf(final Integer rawCode) {
		return Arrays.stream(values())
			.filter(e -> e.getCode().equals(rawCode))
			.findFirst()
			.orElseThrow(NotMatchAuthLevelException::new);
	}
}