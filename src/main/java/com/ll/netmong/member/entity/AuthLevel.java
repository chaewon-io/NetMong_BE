package com.ll.netmong.member.entity;

import java.util.Arrays;

import com.ll.netmong.member.exception.NotMatchAuthLevelException;

import lombok.AllArgsConstructor;
import lombok.Getter;

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